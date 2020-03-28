package com.yuyi.tea.service;

import com.yuyi.tea.bean.Photo;
import com.yuyi.tea.bean.Price;
import com.yuyi.tea.bean.Product;
import com.yuyi.tea.bean.ProductType;
import com.yuyi.tea.mapper.PhotoMapper;
import com.yuyi.tea.mapper.PriceMapper;
import com.yuyi.tea.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CacheConfig(cacheNames = "product")
@Service
public class ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private PriceMapper priceMapper;

    @Autowired
    private PhotoMapper photoMapper;

    @Cacheable(key = "'productTypes'")
    public List<ProductType> getProductTypes(){
        List<ProductType> productTypes = productMapper.getProductTypes();
        return productTypes;
    }

    //获取所有产品的名称
    @Cacheable(key = "'productsName'")
    public List<Product> getProductsNameAndType() {
        List<Product> productsNameByType = productMapper.getProductsNameAndType();
        return productsNameByType;
    }

    //创建新的产品种类
    public void saveProductType(ProductType productType) {
        productMapper.saveProductType(productType);
    }

    //创建新的产品
    public void saveProduct(Product product) {
        priceMapper.savePrice(product.getPrice());
        productMapper.saveProduct(product);
        for(Photo photo:product.getPhotos()){
            photo.setProductId(product.getUid());
            photoMapper.saveProductPhoto(photo);
        }
    }

    //获取产品列表
    public List<Product> getProducts() {
        List<Product> products = productMapper.getProducts();
        return products;
    }

    //下架商品
    public void terminalProduct(int uid) {
        productMapper.terminalProduct(uid);
    }

    //根据uid获取产品信息
    public Product getProduct(int uid) {
        Product product = productMapper.getProduct(uid);
        return product;
    }

    //修改产品信息
    public Product updateProduct(Product product) {
        productMapper.updateProduct(product);
        priceMapper.updatePrice(product.getPrice());
        ArrayList<Integer> photosUid=new ArrayList<Integer>();
        //先更新包厢照片数据
        for(Photo photo:product.getPhotos()){
            photosUid.add(photo.getUid());
            photo.setProductId(product.getUid());
            photoMapper.saveProductPhoto(photo);
        }
        //获取目前所有该产品照片再过滤出过滤后要删除的照片
        List<Photo> originPhotos = photoMapper.getPhotosByProductId(product.getUid());
        List<Photo> needDeletePhotos=originPhotos.stream()
                .filter((Photo photo)->!photosUid.contains(photo.getUid()))
                .collect(Collectors.toList());
        //删除这些照片
        for(Photo photo:needDeletePhotos){
            photoMapper.deletePhoto(photo.getUid());
        }
        List<Photo> currentPhotos = originPhotos.stream()
                .filter((Photo photo)->photosUid.contains(photo.getUid()))
                .collect(Collectors.toList());
        product.setPhotos(currentPhotos);
        return product;
    }
}
