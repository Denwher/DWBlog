package com.dengwei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dengwei.domain.ResponseResult;
import com.dengwei.domain.dto.LinkDto;
import com.dengwei.domain.entity.Link;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2022-08-26 14:29:39
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    ResponseResult getLinkPageList(Integer num, Integer pageNum, String name, String pageSize);

    ResponseResult addLink(LinkDto linkDto);

    ResponseResult getLinkById(Long id);

    ResponseResult updateLink(LinkDto linkDto);

    ResponseResult changeLinkStatus(LinkDto linkDto);

    ResponseResult deleteLink(String id);
}
