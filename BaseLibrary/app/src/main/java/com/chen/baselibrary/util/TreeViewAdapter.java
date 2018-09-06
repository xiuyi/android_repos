package com.chen.baselibrary.util;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chen.baselibrary.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by chen on 2017/6/7.
 * treeView的适配器
 * TreeView的使用方法
 * 1、在布局文件中加入普通ListView布局
 * 2、自定义TreeViewAdapter，实现getContentView方法，在该方法中定义节点样式及内容例如：
 * @Override
 * public View getContentView(Node node, int position, View convertView, ViewGroup viewGroup) {
 *  if(convertView==null)
 *      convertView = this.inflater.inflate(R.layout.tree_listview_item,null);
 *      ImageView indicator = ViewHolder.get(convertView,R.id.id_treenode_icon);
 *      TextView label = ViewHolder.get(convertView,R.id.id_treenode_label);
 *  if(node.getIcon() == -1){
 *      indicator.setVisibility(View.INVISIBLE);
 *  }else{
 *      indicator.setVisibility(View.VISIBLE);
 *      indicator.setImageResource(node.getIcon());
 *  }
 *  label.setText(node.getLabel());
 *  return convertView;
 * }
 * 3、如果需要实现多选或者单选的功能，需要初始化该Adapter后调用setSelectedMode方法，设置单选或者多选，并且需要
 * 实现getCheckBoxId方法，返回自定义的布局文件中的CheckBox的id，注意：只能使用CheckBox组件，可以通过getSelectedNodes
 * 方法获取选中的节点列表
 * 4、设置叶子节点点击事件，调用adapter的setOnLeafNodeClickListener方法，设置监听器
 * 5、设置叶子节点CheckBox被点击时的监听，调用adapter的setOnLeftNodeCheckedClickListener方法，设置监听器
 * 6、自定义收缩、展开按钮：修改NodeHelper的第62、65行
 * 7、自定义不同层级的收缩值：修改本类构造方法中retract的初始化值
 */

public abstract class TreeViewAdapter extends BaseAdapter {
    //以前都是使用ArrayList，但是此处使用的是LinkedList，原因是ArrayList
    //是由数组构造的，此处需要频繁的进行插入、删除数据，性能较低。因此使用LinkedList
    //因为LinkedList使用链表实现，更适用于频繁的插入、删除操作
    private LinkedList<Node> nodeLinkedList;
    private List<Node> allNodeList;
    private int retract;//不同层级之间的缩进值
    private Context context;
    private OnLeafNodeClickListener onLeafNodeClickListener;
    //叶子节点CheckBox被点击时的监听
    private OnLeafNodeCheckedClickListener onLeafNodeCheckedClickListener;
    //选中的节点 key：节点id  value：节点
    private Map<Object,Node> selectedNodes = null;
    //定义选择模式单选，多选，默认
    public enum SelectedMode{
        SINGLE,MULTIPLE,NONE
    }
    private SelectedMode selectedMode = SelectedMode.NONE;

    public TreeViewAdapter(Context context, final ListView listView, LinkedList<Node> linkedList){
        this.context = context;
        this.nodeLinkedList = new LinkedList<>();
        this.nodeLinkedList.addAll(NodeHelper.sortNodes(linkedList));
        this.allNodeList = getAllNodesList_DepthFirst(this.nodeLinkedList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                expandOrCollapse(position);
            }
        });
        retract = (int)(context.getResources().getDisplayMetrics().density*10 +0.5f);
    }

    /**
     * 当用户单击某个节点时，展开或者收缩该节点
     * @param position
     */
    private void expandOrCollapse(int position){
        Node node = nodeLinkedList.get(position);
        //点击非叶子节点
        if(node!=null && !node.isLeaf()){
            if(node.isExpand()){
                List<Node> childNodeList = node.getChildList();
                int childListSize = childNodeList.size();
                Node tempNode = null;
                for(int i=0;i<childListSize;i++){
                    tempNode = childNodeList.get(i);
                    if(tempNode.isExpand()){
                        collapse(tempNode,position+1);
                    }
                    nodeLinkedList.remove(position+1);
                }
            }else{
                nodeLinkedList.addAll(position+1,node.getChildList());
            }
            node.setIsExpand(!node.isExpand());
            notifyDataSetChanged();
        }
        //点击叶子节点
        else if(node!=null && node.isLeaf()){
            if(this.onLeafNodeClickListener!=null)
                this.onLeafNodeClickListener.onLeafNodeClick(node);
        }
    }

    /**
     * 折叠node节点下的所有子节点
     * @param node
     * @param position
     */
    private void collapse(Node node,int position){
        node.setIsExpand(false);
        List<Node> nodes = node.getChildList();
        int size = nodes.size();
        Node tempNode = null;
        for(int i=0;i<size;i++){
            tempNode = nodes.get(i);
            if(tempNode.isExpand()){
                collapse(tempNode,position+1);
            }
            nodeLinkedList.remove(position+1);
        }
    }


    @Override
    public int getCount() {
        return this.nodeLinkedList.size();
    }

    @Override
    public Object getItem(int position) {
        return nodeLinkedList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Node node = nodeLinkedList.get(position);
        convertView = getContentView(node,position,convertView,parent);
        convertView.setPadding(node.getLevel()*retract,5,5,5);
        if(this.selectedMode != SelectedMode.NONE) {
            CheckBox cb = (CheckBox) convertView.findViewById(getCheckboxId());
            //如果是父节点隐藏checkbox
            if (node.isLeaf()) {
                node.setChoose(selectedNodes.containsKey(node.getTreeId()));
                cb.setVisibility(View.VISIBLE);
                cb.setChecked(node.isChoose());
                cb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        node.setChoose(!node.isChoose());
                        if (node.isChoose()) {
                            //如果是单选则取消其他选项
                            if(selectedMode == SelectedMode.SINGLE){
                                selectedNodes.clear();
                            }
                            selectedNodes.put(node.getTreeId(), node);
                        } else {
                            //如果是单选则取消所有选项
                            if(selectedMode == SelectedMode.SINGLE){
                                selectedNodes.clear();
                            }else {
                                selectedNodes.remove(node.getTreeId());
                            }
                        }
                        if(onLeafNodeCheckedClickListener!=null)
                            onLeafNodeCheckedClickListener.onLeafNodeCheckedClick(node);
                        notifyDataSetChanged();
                    }
                });
            } else {
                cb.setVisibility(View.INVISIBLE);
            }
        }
        return convertView;
    }
    /**
     * 设置叶子节点被选中状态切换时的监听
     * @param listener
     */
    public void setOnLeafNodeCheckedClickListener(OnLeafNodeCheckedClickListener listener){
        this.onLeafNodeCheckedClickListener = listener;
    }
    /**
     * 设置叶子节点点击监听器
     * @param listener
     */
    public void setOnLeafNodeClickListener(OnLeafNodeClickListener listener){
        this.onLeafNodeClickListener = listener;
    }

    /**
     * 通过搜索内容进行搜索
     * @param searchStr
     * @return
     */
    public List<Node> searchNode(String searchStr) {
        LinkedList<Node> searchResult = new LinkedList<>();
        if (this.allNodeList != null){
            for (Node node : this.allNodeList) {
                if(node.isMeetSearchCondition(searchStr)) {
                    searchResult.add(node);
                }
            }
        }
        return searchResult;
    }
    /**
     * 将所有的节点组成一个List,使用深度优先算法
     * @param rootList 根节点List
     * @return
     */
    public List<Node> getAllNodesList_DepthFirst(List<Node> rootList){
        Stack<Node> nodeStack = new Stack<>();
        List<Node> allNodes = new LinkedList<>();
        //1.现将根节点压入栈中
        for(Node rootNode:rootList){
            nodeStack.push(rootNode);
        }
        //2.遍历
        while(!nodeStack.isEmpty()){
            Node node = nodeStack.pop();
            allNodes.add(node);
            if(!node.isLeaf()) {
                List<Node> childNodes = node.getChildList();
                for(Node childNode:childNodes){
                    nodeStack.push(childNode);
                }
            }
        }
        return allNodes;
    }

    /**
     * 将所有节点组成一个List，使用广度优先算法
     * @param rootList
     * @return
     */
    public List<Node> getAllNodesList_BreadthFrist(List<Node> rootList){
        Queue<Node> nodeQueue = new LinkedList<>();
        List<Node> allNodes = new LinkedList<>();
        nodeQueue.addAll(rootList);
        while(!nodeQueue.isEmpty()){
            Node node = nodeQueue.poll();
            allNodes.add(node);
            if(!node.isLeaf()){
                List<Node> childNodes = node.getChildList();
                for(Node childNode:childNodes){
                    nodeQueue.offer(childNode);
                }
            }
        }
        return allNodes;
    }
    /**
     * 获取选中的节点列表
     */
    public List<Node> getSelectedNodes(){
        if(this.selectedNodes!=null){
            return new ArrayList<>(this.selectedNodes.values());
        }else{
            return null;
        }
    }
    /**
     * 设置选择模式
     * @param mode
     */
    public void setSelectedMode(SelectedMode mode){
        this.selectedMode = mode;
        if(mode!=SelectedMode.NONE){
            this.selectedNodes = new HashMap<>();
        }
    }
    public abstract View getContentView(Node node,int position,View convertView,ViewGroup viewGroup);

    /**
     * 如果需要选择，则需要提供CheckBox的id，不需要选择返回任意数字即可
     * @return
     */
    public abstract int getCheckboxId();

    public interface OnLeafNodeClickListener{
        void onLeafNodeClick(Node node);
    }

    /**
     * 选择框被点击的监听
     */
    public interface OnLeafNodeCheckedClickListener{
        void onLeafNodeCheckedClick(Node node);
    }
}
