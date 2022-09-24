package com.dengwei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dengwei.constant.SystemConstants;
import com.dengwei.domain.ResponseResult;
import com.dengwei.domain.dto.LinkDto;
import com.dengwei.domain.entity.Link;
import com.dengwei.domain.enums.AppHttpCodeEnum;
import com.dengwei.domain.vo.LinkVo;
import com.dengwei.domain.vo.PageVo;
import com.dengwei.exception.SystemException;
import com.dengwei.mapper.LinkMapper;
import com.dengwei.service.LinkService;
import com.dengwei.util.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2022-08-26 14:29:39
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    @Override
    public ResponseResult getAllLink() {
        LambdaQueryWrapper<Link> wrapper = new LambdaQueryWrapper<>();
        //审核通过的友链才可以展示
        wrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_ACCEPTED);
        //根据条件查询
        List<Link> linkList = list(wrapper);
        //封装到vo
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(linkList, LinkVo.class);
        return ResponseResult.okResult(linkVos);
    }

    @Override
    public ResponseResult getLinkPageList(Integer pageNum, Integer pageSize, String name, String status) {
        LambdaQueryWrapper<Link> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(name),Link::getName,name)
                .eq(Objects.nonNull(status)
                                && (status.equals(SystemConstants.LINK_STATUS_ACCEPTED) ||
                                status.equals(SystemConstants.LINK_STATUS_UNREAD) ||
                                status.equals(SystemConstants.LINK_STATUS_DENIED)),
                        Link::getStatus,status);
        //分页
        Page<Link> page = new Page<>(pageNum,pageSize);
        page(page,wrapper);
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(page.getRecords(), LinkVo.class);
        return ResponseResult.okResult(new PageVo(linkVos,page.getTotal()));
    }

    @Override
    public ResponseResult addLink(LinkDto linkDto) {
        //对LinkDetailDto参数校验
        if(Objects.isNull(linkDto) ||
                !StringUtils.hasText(linkDto.getName()) ||
                !StringUtils.hasText(linkDto.getLogo()) ||
                !StringUtils.hasText(linkDto.getAddress()) ||
                !StringUtils.hasText(linkDto.getDescription())){
            throw new SystemException(AppHttpCodeEnum.PARAMS_CANNOT_BE_NULL);
        }
        Link link = BeanCopyUtils.copyBean(linkDto, Link.class);
        save(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getLinkById(Long id) {
        Link link = getById(id);
        if(Objects.isNull(link)){
            throw new SystemException(AppHttpCodeEnum.LINK_NOT_EXIST);
        }
        LinkVo linkVo = BeanCopyUtils.copyBean(link, LinkVo.class);
        return ResponseResult.okResult(linkVo);
    }

    @Override
    public ResponseResult updateLink(LinkDto linkDto) {
        //对LinkDetailDto参数校验
        if(Objects.isNull(linkDto) ||
                !StringUtils.hasText(linkDto.getName()) ||
                !StringUtils.hasText(linkDto.getLogo()) ||
                !StringUtils.hasText(linkDto.getAddress()) ||
                !StringUtils.hasText(linkDto.getDescription())){
            throw new SystemException(AppHttpCodeEnum.PARAMS_CANNOT_BE_NULL);
        }
        //更新友链
        Link link = BeanCopyUtils.copyBean(linkDto, Link.class);
        updateById(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult changeLinkStatus(LinkDto linkDto) {
        Long id = linkDto.getId();
        String status = linkDto.getStatus();
        if(!StringUtils.hasText(status) && Objects.nonNull(id)){
            throw new SystemException(AppHttpCodeEnum.PARAMS_CANNOT_BE_NULL);
        }
        Link link = getById(id);
        if(Objects.isNull(link)){
            throw new SystemException(AppHttpCodeEnum.LINK_NOT_EXIST);
        }
        //更新状态
        link.setStatus(status);
        updateById(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteLink(String id) {
        if(!StringUtils.hasText(id)){
            throw new SystemException(AppHttpCodeEnum.ID_CANNOT_EMPTY);
        }
        List<Long> ids = Arrays.stream(id.split(","))
                .map(s -> Long.valueOf(s))
                .collect(Collectors.toList());
        //使用逻辑删除
        removeByIds(ids);
        return ResponseResult.okResult();
    }
}
