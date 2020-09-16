package com.leyou.item.service;

import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecificationService {
    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Autowired
    private SpecParamMapper paramMapper;


    //根据分类id查询分组

    public List<SpecGroup> queryGroupsByCid(Long cid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        return this.specGroupMapper.select(specGroup);
    }


    /**
     * 根据条件查询规格参数
     *
     * @param gid
     * @return
     */
    public List<SpecParam> queryParams(Long gid) {
        SpecParam param = new SpecParam();
        param.setGroupId(gid);
        return this.paramMapper.select(param);
    }

    /**
     * 新增分组
     * @param specGroup
     */
    public void addGroup(SpecGroup specGroup) {
        this.specGroupMapper.insert(specGroup);
    }

    //新增参数
    public void addParam(SpecParam specParam){
        this.paramMapper.insert(specParam);
    }

    //修改分组
    public void updateGroup(SpecGroup specGroup){
        this.specGroupMapper.updateByPrimaryKey(specGroup);
    }


    //修改参数
    public void updateParam(SpecParam specParam){
        this.paramMapper.updateByPrimaryKey(specParam);
    }


    //删除分组
    public void deleteGroup(Long id) {
        this.specGroupMapper.deleteByPrimaryKey(id);
    }

    //删除参数
    public void deleteParam(Long id){
        this.paramMapper.deleteByPrimaryKey(id);
    }

    /**
     * 新增商品----根据gid查询规格参数
     * @param gid
     * @return
     */
    public List<SpecParam> queryParams(Long gid, Long cid, Boolean generic, Boolean searching) {
        SpecParam record = new SpecParam();
        record.setGroupId(gid);
        record.setCid(cid);
        record.setGeneric(generic);
        record.setSearching(searching);
        return this.paramMapper.select(record);
    }


    public List<SpecGroup> queryGroupsWithParam(Long cid) {
        // 查询规格组
        List<SpecGroup> groups = this.queryGroupsByCid(cid);
        groups.forEach(g -> {
            // 查询组内参数
            List<SpecParam>params=this.queryParams(g.getId(), null, null, null);
            g.setParams(params);
        });
        return groups;
    }
}
