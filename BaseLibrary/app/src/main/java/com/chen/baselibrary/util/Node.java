package com.chen.baselibrary.util;

import com.chen.baselibrary.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen on 2017/6/7.
 * treeView的节点抽象类，泛型T主要是考虑到id和pId有可能是int类型也有可能是String类型
 * 这里可传入Integer或者String
 */

public abstract class Node<T> implements Serializable{
    private int _level = -1;//当前节点的层级，初始为-1
    private List<Node> _childList = new ArrayList<>();//所有子节点
    private Node _parent;//父节点
    private int _icon;//图标资源ID
    private boolean isExpand = false;//当前是否是展开状态
    private boolean isChoose = false;//是否被选中

    public abstract T getTreeId();//获取当前节点ID
    public abstract T getTreeParentId();//获取父节点
    public abstract String getLabel();//获取要显示的内容
    public abstract boolean isParent(Node dest);//判断当前节点是否是dest的父节点
    public abstract boolean isChild(Node dest);//判断当前节点是否是dest的子节点
    public abstract boolean isMeetSearchCondition(String condition);//判断节点是否符合condition定义的筛选条件

    /**
     * 获取当前节点的层级
     * @return
     */
    public int getLevel(){
        if(this._level == -1){
            int level = _parent == null?1:_parent.getLevel()+1;
            this._level = level;
            return this._level;
        }
        return this._level;
    }

    /**
     * 设置当前节点层级
     * @param level
     */
    public void setLevel(int level){
        this._level = level;
    }

    /**
     * 获取子节点
     * @return
     */
    public List<Node> getChildList(){
        return _childList;
    }

    /**
     * 设置子节点
     * @param childList
     */
    public void setChildList(List<Node> childList){
        this._childList = childList;
    }

    /**
     * 获取父节点
     * @return
     */
    public Node getParent(){
        return this._parent;
    }

    /**
     * 设置父节点
     * @param node
     */
    public void setParent(Node node){
        this._parent = node;
    }

    /**
     * 获取图标
     * @return
     */
    public int getIcon(){
        return _icon;
    }

    /**
     * 设置图标
     */
    public void setIcon(int icon){
        this._icon = icon;
    }

    /**
     * 判断当前节点是否是展开状态
     * @return
     */
    public boolean isExpand(){
        return this.isExpand;
    }

    /**
     * 设置展开状态
     * @param isExpand
     */
    public void setIsExpand(boolean isExpand){
        this.isExpand = isExpand;
        if(isExpand)
            this._icon = R.drawable.collapse;
        else
            this._icon = R.drawable.expand;
    }

    /**
     * 判断是否是根节点
     * @return
     */
    public boolean isRoot(){
        return _parent == null;
    }

    /**
     * 判断是否是叶子节点
     * 叶子节点没有子节点
     * @return
     */
    public boolean isLeaf(){
        return this._childList.size()==0;
    }

    /**
     * 判断是否被选中
     * @return
     */
    public boolean isChoose(){return isChoose;};

    /**
     * 设置选中状态
     * @param isChoose
     */
    public void setChoose(boolean isChoose){
        this.isChoose = isChoose;
        /*
        //如果是父节点并且是选中状态则选中其下的所有子节点
        if(!isLeaf() && isChoose){
            for(Node cNode:getChildList()){
                cNode.setChoose(true);
            }
        }
        //如果是父节点并且是取消选中状态则取消选中其下的所有子节点
        else if(!isLeaf() && !isChoose){
            for(Node cNode:getChildList()){
                cNode.setChoose(false);
            }
        }
        //如果是选中状态，则检查其所有兄弟节点是否都被选中，如果都被选中，则选中他们的父节点
        if(isChoose){
            boolean isAllChoose = true;
            List<Node> sList = this._parent.getChildList();
            for(Node cNode:sList){
                if(!cNode.isChoose())
                    isAllChoose = false;
            }
            if(isAllChoose){
                this._parent.setChoose(true);
            }
        }
        //如果是取消选中，则取消选中其父节点
        else{
            this._parent.setChoose(false);
        }*/
    }

}
