package com.yuyi.tea.common;

import com.yuyi.tea.bean.Article;
import com.yuyi.tea.bean.Product;
import com.yuyi.tea.bean.ShopBox;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchResult implements Serializable {

    private List<Article> articles=new ArrayList<>();
    private List<Product> products=new ArrayList<>();
    private List<ShopBox> boxes=new ArrayList<>();

    public SearchResult(List<Article> articles, List<Product> products, List<ShopBox> boxes) {
        this.articles = articles;
        this.products = products;
        this.boxes = boxes;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<ShopBox> getBoxes() {
        return boxes;
    }

    public void setBoxes(List<ShopBox> boxes) {
        this.boxes = boxes;
    }
}
