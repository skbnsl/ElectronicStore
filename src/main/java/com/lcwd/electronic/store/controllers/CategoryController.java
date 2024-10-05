package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.*;
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.services.CategoryService;
import com.lcwd.electronic.store.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    @Value("${user.profile.imageCategories.path}")
    private String categoryImageUploadPath;

    @Autowired
    private FileService fileService;

    //create
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto){
        //call service to save object
        CategoryDto categoryDto1 = categoryService.create(categoryDto);
        return new ResponseEntity<>(categoryDto1, HttpStatus.CREATED);
    }

    //update
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto categoryDto, @PathVariable String categoryId){
         CategoryDto updatedCategory = categoryService.update(categoryDto, categoryId);
         return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }


    //delete
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String categoryId){
        categoryService.delete(categoryId);
         ApiResponseMessage responseMessage = ApiResponseMessage.builder().message("Category is deleted successfully!!").status(HttpStatus.OK).success(true).build();
         return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }

    //getAll
    @GetMapping
    public ResponseEntity<PageableResponse<CategoryDto>> getAll(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        PageableResponse<CategoryDto> pageableResponse = categoryService.getAll(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }


    //single
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getSingle (@PathVariable String categoryId){
         CategoryDto categoryDto = categoryService.get(categoryId);
         return ResponseEntity.ok(categoryDto);
    }

    //upload user Image
    @PostMapping("/categoryImage/{categoryId}")
    public ResponseEntity<ImageResponse> uploadUserImage(@RequestParam("categoryImage") MultipartFile image, @PathVariable String categoryId) throws IOException {
        String imageName = fileService.uploadFileCategory(image, categoryImageUploadPath);
        CategoryDto category = categoryService.get(categoryId);
        category.setCoverImage(imageName);
        CategoryDto userDto = categoryService.update(category,categoryId);
        ImageResponse imageResponse = ImageResponse.builder().imageName(imageName).success(true).status(HttpStatus.CREATED).build();
        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
    }

    //serve user image
    @GetMapping("/categoryImage/{categoryId}")
    public void serveUserImage(@PathVariable String categoryId, HttpServletResponse response) throws IOException {
        logger.info("saveUserImage:: {}",categoryId);
        //fetch user
        CategoryDto categoryDto = categoryService.get(categoryId);
        InputStream resource = fileService.getResourceCategory(categoryImageUploadPath, categoryDto.getCoverImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }

}
