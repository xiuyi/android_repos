package com.chen.baselibrary.util;

import com.chen.baselibrary.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen on 2017/6/7.
 * treeView组件的节点帮助类
 */

public class NodeHelper {
    /**
     * 对所有节点进行遍历，生成根节点的List，生成之后的根节点
     * 已经设置好了子节点
     * @param nodeList
     * @return
     */
    public static List<Node> sortNodes(List<Node> nodeList){
        List<Node> rootNodes = new ArrayList<>();
        int size = nodeList.size();
        Node m;
        Node n;
        //两层的for循环整理出所有数据之间的父子关系，最后会构造成一个森林
        for(int i=0;i<size;i++){
            m = nodeList.get(i);
            for(int j=i+1;j<size;j++){
                n = nodeList.get(j);
                if(m.isParent(n)){
                    m.getChildList().add(n);
                    n.setParent(m);
                }else if(m.isChild(n)){
                    n.getChildList().add(m);
                    m.setParent(n);
                }
            }
        }
        //找出所有的根节点，同时设置相应的图标
        for(int i=0;i<size;i++){
            m = nodeList.get(i);
            if(m.isRoot()){
                rootNodes.add(m);
            }
            setNodeIcon(m);
        }
        nodeList.clear();
        nodeList = rootNodes;
        rootNodes = null;
        return nodeList;
    }

    /**
     * 设置节点图标
     * @param node
     */
    public static void setNodeIcon(Node node){
        //非叶子节点显示图标
        if(!node.isLeaf()){
            if(node.isExpand()) {
                //设置收缩按钮
                node.setIcon(R.drawable.collapse);
            }else {
                //设置展开按钮
                node.setIcon(R.drawable.expand);
            }
        }
        //叶子节点不显示图标
        else{
            node.setIcon(-1);
        }
    }
}
