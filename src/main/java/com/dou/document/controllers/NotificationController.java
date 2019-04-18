package com.dou.document.controllers;

import com.dou.adm.security.JwtUser;
import com.dou.adm.shared.ResponseObject;
import com.dou.document.services.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static com.dou.adm.security.JwtAuthFilter.JWT_USER;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private INotificationService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseObject fetchNotify(@RequestParam("page") int page, @RequestAttribute(JWT_USER) JwtUser user) {
        return service.fetchNotify(user.getUsername(), page);
    }

    @GetMapping(value = "/unread", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseObject retrieveUnread(@RequestAttribute(JWT_USER) JwtUser user) {
        return service.retrieveUnread(user.getUsername());
    }

    @PutMapping(value = "/{id}")
    public ResponseObject changeReadState(@PathVariable("id") int id, @RequestAttribute(JWT_USER) JwtUser user) {
        return service.changeReadState(user.getUsername(), id);
    }

    @PutMapping(value = "/all-notify")
    public ResponseObject changeAllMessageState(@RequestAttribute(JWT_USER) JwtUser user) {
        return service.changeAllMessageState(user.getUsername());
    }
}
