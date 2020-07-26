package org.hackDefender.controller.backend;

import org.hackDefender.common.ServerResponse;
import org.hackDefender.interceptor.Permission;
import org.hackDefender.interceptor.RequestLogin;
import org.hackDefender.pojo.Category;
import org.hackDefender.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author vvings
 * @version 2020/4/21 11:05
 */
@Controller
@RequestMapping("/manage/category/")
@RequestLogin(desc = Permission.REQUEST_ADMIN)
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value = "add_category.do", method = RequestMethod.POST)
    @ResponseBody
    @RequestLogin(desc = Permission.REQUEST_ADMIN)
    public ServerResponse addCategory(String categoryName, @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
        return categoryService.addCategory(categoryName, parentId);
    }

    @RequestMapping(value = "get_deep_category.do", method = RequestMethod.POST)
    @ResponseBody
    @RequestLogin(desc = Permission.REQUEST_ADMIN)
    public ServerResponse<List<Category>> getChildren(Integer categoryId) {
        return categoryService.selectCategoryAndChildrenById(categoryId);
    }

    @RequestMapping(value = "get_category.do", method = RequestMethod.POST)
    @ResponseBody
    @RequestLogin(desc = Permission.REQUEST_ADMIN)
    public ServerResponse<List<Category>> getCategory(@RequestParam(value = "parentId", defaultValue = "0") Integer parentId) {
        return categoryService.getChildrenCategory(parentId);
    }

    @RequestMapping(value = "update_category.do", method = RequestMethod.POST)
    @ResponseBody
    @RequestLogin(desc = Permission.REQUEST_ADMIN)
    public ServerResponse<List<Category>> updateCategory(Integer categoryId, String categoryName) {
        return categoryService.updateCategory(categoryId, categoryName);
    }

    @RequestMapping(value = "del_category.do", method = RequestMethod.POST)
    @ResponseBody
    @RequestLogin(desc = Permission.REQUEST_ADMIN)
    public ServerResponse<List<Category>> delCategory(Integer categoryId) {
        return categoryService.delCategory(categoryId);
    }
}
