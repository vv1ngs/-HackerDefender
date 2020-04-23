package org.hackDefender.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hackDefender.common.ServerResponse;
import org.hackDefender.dao.CategoryMapper;
import org.hackDefender.pojo.Category;
import org.hackDefender.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author vvings
 * @version 2020/4/21 11:12
 */
@Slf4j
@Service("categoryMapper")
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ServerResponse addCategory(String CategoryName, Integer parentId) {
        if (StringUtils.isBlank(CategoryName)) {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        Category category = new Category();
        category.setName(CategoryName);
        category.setParentId(parentId);
        int rowCount = categoryMapper.insert(category);
        if (rowCount > 0) {
            return ServerResponse.createBySuccess("添加分类成功");
        }
        return ServerResponse.createByErrorMessage("添加分类失败");
    }

    @Override
    public ServerResponse<List<Category>> getChildrenCategory(Integer categoryId) {
        List<Category> categoryList = categoryMapper.selectChildrenByCategoryById(categoryId);
        if (CollectionUtils.isEmpty(categoryList)) {
            log.info("没有该分类");
        }
        return ServerResponse.createBySuccess(categoryList);
    }
}
