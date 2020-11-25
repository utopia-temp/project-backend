package com.project.backend.service.impl;

import com.project.backend.common.ServerResponse;
import com.project.backend.dao.CategoryMapper;
import com.project.backend.pojo.Category;
import com.project.backend.service.ICategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Utopia
 */
@Service("iCategoryService")
public class ICategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 添加商品种类
     *
     * @param categoryName 商品种类名称
     * @param parentId     商品父类id
     * @return 服务响应对象
     */
    @Override
    public ServerResponse<String> addCategory(String categoryName, Integer parentId) {
        //判断参数合法性
        if (StringUtils.isBlank(categoryName) || parentId == null) {
            return ServerResponse.createByErrorMessage("参数错误");
        }

        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        //设置商品分类状态为“正常”
        category.setStatus(true);

        int count = categoryMapper.insert(category);
        if (count > 0) {
            return ServerResponse.createBySuccessMessage("添加商品种类成功");
        }
        return ServerResponse.createByErrorMessage("添加商品种类失败");
    }

    /**
     * 更新商品分类名称
     *
     * @param categoryName 商品分类名称
     * @param categoryId   商品ID
     * @return 服务响应对象
     */
    @Override
    public ServerResponse<String> updateCategoryName(String categoryName, Integer categoryId) {
        //判断参数合法性
        if (StringUtils.isBlank(categoryName) || categoryId == null) {
            return ServerResponse.createByErrorMessage("参数错误");
        }

        Category category = new Category();
        //设置商品Id，通过ID进行更新
        category.setId(categoryId);
        category.setName(categoryName);

        int count = categoryMapper.updateByPrimaryKeySelective(category);
        if (count > 0) {
            return ServerResponse.createBySuccessMessage("修改商品种类名称成功");
        }
        return ServerResponse.createByErrorMessage("修盖商品种类名称失败");
    }
}
