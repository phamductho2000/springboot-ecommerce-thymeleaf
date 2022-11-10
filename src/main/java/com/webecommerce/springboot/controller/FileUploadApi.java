//package com.webecommerce.springboot.controller;
//
//import com.webecommerce.springboot.response.ResponseObject;
//import com.webecommerce.springboot.service.StorageService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping(path = "/api/v1/fileupload")
//public class FileUploadApi {
//    @Autowired
//    private StorageService storageService;
//
//    @PostMapping("")
//    public ResponseEntity<ResponseObject> uploadFile(@RequestParam("file") MultipartFile file) {
//        try {
//            //save files to a folder => use a service
//            String generatedFileName = storageService.storeFile(file);
//            return ResponseEntity.status(HttpStatus.OK).body(
//                    new ResponseObject("ok", "Upload file successfully", generatedFileName)
//            );
//            //06a290064eb94a02a58bfeef36002483.png => how to open this file in Web Browser ?
//        } catch (Exception exception) {
//            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
//                    new ResponseObject("ok", exception.getMessage(), "")
//            );
//        }
//    }
//
//    @GetMapping("/files/{fileName:.+}")
//    // /files/06a290064eb94a02a58bfeef36002483.png
//    public ResponseEntity<byte[]> readDetailFile(@PathVariable String fileName) {
//        try {
//            byte[] bytes = storageService.readFileContent(fileName);
//            return ResponseEntity
//                    .ok()
//                    .contentType(MediaType.IMAGE_JPEG)
//                    .body(bytes);
//        } catch (Exception exception) {
//            return ResponseEntity.noContent().build();
//        }
//    }
//
////    @GetMapping("")
////    public ResponseEntity<ResponseObject> getUploadedFiles() {
////        try {
////            List<String> urls = storageService.loadAll()
////                    .map(path -> {
////                        //convert fileName to url(send request "readDetailFile")
////                       String urlPath = MvcUriComponentsBuilder.fromMethodName(FileUploadApi.class,
////                                "readDetailFile", path.getFileName().toString()).build().toUri().toString();
////                        String urlPath = path.getParent().toString();
////                        return urlPath;
////                    })
////                    .collect(Collectors.toList());
////            return ResponseEntity.ok(new ResponseObject("ok", "Query list files successfully", urls));
////        } catch (Exception exception) {
////            return ResponseEntity.ok(new
////                    ResponseObject("failed", "Query list files failed", new String[]{}));
////        }
////    }
//}
