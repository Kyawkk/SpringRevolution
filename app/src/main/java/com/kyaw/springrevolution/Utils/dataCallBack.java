package com.kyaw.springrevolution.Utils;

import com.kyaw.springrevolution.Model.Post;

import java.util.ArrayList;

public interface dataCallBack {
    public void onReceived(ArrayList<Post> posts);
}
