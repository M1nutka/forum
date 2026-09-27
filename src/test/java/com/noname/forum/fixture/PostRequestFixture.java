package com.noname.forum.fixture;

import com.noname.forum.dto.post.PostRequest;

public class PostRequestFixture {
    
    public static PostRequest defaultPost(){

        return new  PostRequest(
            "My first post!",
            "Tu tu ru"
        );
    }
}
