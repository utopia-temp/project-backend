package com.project.backend.service;

import com.project.backend.common.ServerResponse;

/**
 * @author Utopia
 */
public interface ICategoryService {
    /**
     * 添加商品种类
     *
     * @param categoryName 商品种类名称
     * @param parentId     商品父类id
     * @return 服务响应对象
     */
    ServerResponse<String> addCategory(String categoryName, Integer parentId);

    /**
     * 更新商品分类名称
     *
     * @param categoryName 商品分类名称
     * @param categoryId   商品ID
     * @return 服务响应对象
     */
    ServerResponse<String> updateCategoryName(String categoryName, Integer categoryId);
}
