package com.dengwei.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dengwei.domain.entity.Menu;
import com.dengwei.domain.vo.MenuTreeVo;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author makejava
 * @since 2022-08-30 09:53:45
 */
public interface MenuMapper extends BaseMapper<Menu> {

    @Select("select distinct m.perms from sys_user_role ur, sys_role_menu rm, sys_menu m " +
            "where " +
            "ur.user_id = #{userId} and ur.role_id = rm.role_id and m.menu_type in ('C','F') and m.id = rm.menu_id and m.del_flag = 0")
    List<String> queryPermsByUserId(Long userId);

    @Select("select distinct id, parent_id, menu_name, path, component, visible, `status`," +
            " IFNULL(perms,'') AS perms, is_frame, menu_type, icon, order_num, create_time " +
            "from sys_menu where del_flag = 0 and `status` = 0 order by parent_id and order_num")
    List<Menu> selectAllRouterMenu();

    @Select("select distinct id, parent_id, menu_name as label, order_num from sys_menu where del_flag = 0 and `status` = 0 order by parent_id and order_num")
    List<MenuTreeVo> selectMenuTree();

    @Select("SELECT DISTINCT m.id, m.parent_id, m.menu_name, m.path, m.component, m.visible, m.status, IFNULL(m.perms,'') AS perms, " +
            " m.is_frame,  m.menu_type, m.icon, m.order_num, m.create_time, ur.user_id" +
            " FROM " +
            " `sys_user_role` ur" +
            " LEFT JOIN `sys_role_menu` rm ON ur.`role_id` = rm.`role_id`" +
            " LEFT JOIN `sys_menu` m ON m.`id` = rm.`menu_id`" +
            " WHERE" +
            " ur.`user_id` = #{userId} AND" +
            " m.`menu_type` IN ('C','M') AND" +
            " m.`status` = 0 AND" +
            " m.`del_flag` = 0" +
            " ORDER BY" +
            " m.parent_id,m.order_num")
    List<Menu> selectRouterMenuTreeByUserId(Long userId);
}
