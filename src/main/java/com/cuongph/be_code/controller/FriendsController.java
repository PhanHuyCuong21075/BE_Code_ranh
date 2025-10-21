package com.cuongph.be_code.controller;


import com.cuongph.be_code.response.ResponseData;
import com.cuongph.be_code.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendsController {

    private final FriendService friendService;

    @GetMapping("/list/{username}")
    public ResponseData<Object> getFriends(@PathVariable String username) {
        return new ResponseData<>().success(friendService.getFriends(username));

    }

    @GetMapping("/suggest/{username}")
    public ResponseData<Object> suggestFriends(@PathVariable String username) {
        return new ResponseData<>().success(friendService.suggestFriends(username));
    }
}
