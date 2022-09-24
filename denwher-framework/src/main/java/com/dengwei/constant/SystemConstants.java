package com.dengwei.constant;

/**
 * @author Denwher
 * @version 1.0
 *
 * 实际项目中都不允许直接在代码中使用字面值。都需要定义成常量来使用
 */
public class SystemConstants {

    //文章是草稿
    public static final int ARTICLE_STATUS_DRAFT = 1;
    //文章是正常分布状态
    public static final int ARTICLE_STATUS_NORMAL = 0;

    //标签状态0:正常
    public static final String CATEGORY_STATUS_NORMAL = "0";
    //标签状态1:禁用
    public static final String CATEGORY_STATUS_FORBIDDEN = "1";

    //友链审核状态 (0代表审核通过，1代表审核未通过，2代表未审核)
    public static final String LINK_STATUS_ACCEPTED = "0";
    public static final String LINK_STATUS_DENIED = "1";
    public static final String LINK_STATUS_UNREAD = "2";

    //redis存入前台登录key的前缀
    public static final String REDIS_LOGIN_KEY_PREFIX = "bloglogin:";
    //redis存入后台登录key的前缀
    public static final String REDIS_USER_LOGIN_KEY_PREFIX = "login:";
    //redis存入获取文章viewCount的key的前缀
    public static final String REDIS_VIEW_COUNT_KEY_PREFIX = "article:viewCount";

    //评论类型（0代表文章评论，1代表友链评论）
    public static final String ARTICLE_COMMENT = "0";
    public static final String LINK_COMMENT = "1";

    //菜单类型（M目录 C菜单 F按钮）
    public static final String BUTTON = "F";
    public static final String MENU = "C";
    public static final String CATALOG = "M";

    //菜单状态（0正常 1停用）
    public static final String MENU_STATUS_NORMAL = "0";
    public static final String MENU_STATUS_FORBIDDEN = "1";


    /**
     * 根评论
     */
    public static final int ROOT_COMMENT = -1;

    //后台用户type
    public static final String ADMIN = "1";
    //前台用户type
    public static final String NOT_ADMIN = "0";

}
