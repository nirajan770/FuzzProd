package com.fuzzprod.api;

import com.fuzzprod.api.model.Item;

import java.util.List;

import retrofit.http.GET;
import rx.Observable;


/**
 * Created by Nirajan on 10/20/2015.
 */
public interface FuzzAPI {

    @GET("/quizzes/mobile/1/data.json")
    Observable<List<Item>> items();

}
