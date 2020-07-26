package org.hackDefender.service.Impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
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
import java.util.Set;

/**
 * @author vvings
 * @version 2020/4/21 11:12
 */
@Slf4j
@Service("categoryService")
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
    public ServerResponse<List<Category>> getChildrenCategory(Integer parentId) {
        List<Category> categoryList = categoryMapper.selectChildrenByCategoryById(parentId);
        if (CollectionUtils.isEmpty(categoryList)) {
            log.info("没有该分类");
        }
        return ServerResponse.createBySuccess(categoryList);
    }

    @Override
    public ServerResponse<List<Category>> updateCategory(Integer categoryId, String categoryName) {
        if (categoryId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setId(categoryId);
        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if (rowCount > 0) {
            return ServerResponse.createBySuccess("更新类名成功");
        }
        return ServerResponse.createByErrorMessage("更新类名失败");
    }

    @Override
    public ServerResponse<List<Category>> delCategory(Integer categoryId) {
        if (categoryId == null) {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        List<Category> categoryList = categoryMapper.selectChildrenByCategoryById(categoryId);
        for (Category category : categoryList) {
            categoryMapper.deleteByPrimaryKey(category.getId());
        }
        categoryMapper.deleteByPrimaryKey(categoryId);
        return ServerResponse.createBySuccess("删除成功");
    }

    @Override
    public ServerResponse selectCategoryAndChildrenById(Integer parentId) {
        Set<Category> categorySet = Sets.newHashSet();
        findChildCategory(categorySet, parentId);
        List<Integer> categoryIdList = Lists.newArrayList();
        if (parentId != null) {
            for (Category categoryItem : categorySet) {
                categoryIdList.add(categoryItem.getId());
            }
        }
        return ServerResponse.createBySuccess(categoryIdList);
    }

    private Set<Category> findChildCategory(Set<Category> categorySet, Integer categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null) {
            categorySet.add(category);
        }
        List<Category> categoryList = categoryMapper.selectChildrenByCategoryById(categoryId);
        for (Category categoryItem : categoryList) {
            findChildCategory(categorySet, categoryItem.getId());
        }
        return categorySet;
    }
}
