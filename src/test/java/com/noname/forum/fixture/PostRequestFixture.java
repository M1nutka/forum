package com.noname.forum.fixture;

import com.noname.forum.dto.post.PostRequest;

public class PostRequestFixture {
    
    public static PostRequest defaultPost(){
        return new  PostRequest(
            "My first post!",
            "Tu tu ru"
        );
    }

    public static PostRequest defaultPost2(){
        return new  PostRequest(
            "My second post!",
            "Lorem orem impale"
        );
    }

    public static PostRequest incorrectPost(){
        return new  PostRequest(
            null,
            "Tu"
        );
    }

}
