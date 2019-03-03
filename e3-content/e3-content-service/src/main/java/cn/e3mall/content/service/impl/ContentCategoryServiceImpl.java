package cn.e3mall.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentCategoryService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import cn.e3mall.pojo.TbContentCategoryExample.Criteria;



/***
 * 内容分类管理Service
 * @author tao
 *
 */	
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;
	
	@Override
	public List<EasyUITreeNode> getContentCatList(long parentId) {
		// 根据parentId查询子节点列表
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		//设置查询条件
		criteria.andParentIdEqualTo(parentId);
		//查询
		List<TbContentCategory> catList = contentCategoryMapper.selectByExample(example);
		//转换成EasyUITreeNode的列表
		List<EasyUITreeNode> nodeList = new ArrayList<EasyUITreeNode>();
		for (TbContentCategory tbContentCategory : catList) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbContentCategory.getId());
			node.setText(tbContentCategory.getName());
			node.setState(tbContentCategory.getIsParent()?"closed":"open");
			//
			nodeList.add(node);
		}
		return nodeList;
	}

	@Override
	public E3Result addContentCategory(long parentId, String name) {
		//创建一个tb_contentcategory表对应的pojo
		TbContentCategory contentCategory = new TbContentCategory();
		//设置pojo的属性
		contentCategory.setParentId(parentId);
		contentCategory.setName(name);
		//1正常、2删除
		contentCategory.setStatus(1);
		//新添加的节点一定是叶子结点
		contentCategory.setIsParent(false);
		contentCategory.setSortOrder(1);
		contentCategory.setCreated(new Date());
		contentCategory.setUpdated(new Date());
		//插入数据库
		contentCategoryMapper.insertSelective(contentCategory);
		//判断父节点isparent属性 不是true改为true
		//根据parentid查询父节点
		TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
		if(!parent.getIsParent()){
			parent.setIsParent(true);
			//更新到数据库
			contentCategoryMapper.updateByPrimaryKey(parent);
		}
		//返回需要的
		return E3Result.ok(contentCategory);
	}

	@Override
	public void deleteContentCategory(long id) {
		contentCategoryMapper.deleteByPrimaryKey(id);
		TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(id);
		TbContentCategoryExample contentCategoryExample = new TbContentCategoryExample();
		Criteria criteria = contentCategoryExample.createCriteria();
		criteria.andParentIdEqualTo(parent.getId());
		int countByExample = contentCategoryMapper.countByExample(contentCategoryExample);
		if(countByExample==0){
			parent.setIsParent(false);
			contentCategoryMapper.updateByPrimaryKey(parent);
		}
	}

	@Override
	public void updateContentCategory(long id, String name) {
		TbContentCategory contentCategory = new TbContentCategory();
		contentCategory.setName(name);
		contentCategoryMapper.updateByPrimaryKeySelective(contentCategory);
	}

}
