/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package net.sf.click.examples.page.tree;

import net.sf.click.examples.page.BorderPage;
import net.sf.click.extras.tree.Tree;
import net.sf.click.extras.tree.TreeNode;

/**
 * Example usage of the {@link Tree} control.
 *
 * @author Bob Schellink
 */
public class PlainTreePage extends BorderPage {

    public static final String TREE_NODES_SESSION_KEY = "treeNodes";

    protected Tree tree;

    // --------------------------------------------------------- Event Handlers

    /**
     * @see net.sf.click.Page#onInit()
     */
    public void onInit() {
        super.onInit();

        //Create the tree and tree model and add it to the page
        tree = buildTree();
        addControl(tree);
    }

    /**
     * @see net.sf.click.Page#onGet()
     */
    public void onGet() {
        String selectId = getContext().getRequestParameter(Tree.SELECT_TREE_NODE_PARAM);
        String expandId = getContext().getRequestParameter(Tree.EXPAND_TREE_NODE_PARAM);
        if(selectId == null && expandId == null) {
            return;
        }

        TreeNode node = null;
        if(selectId != null) {
            node = tree.find(selectId);
        } else {
            node = tree.find(expandId);
        }
        addModel("treeNode", node);
    }

    // --------------------------------------------------------- Protected Methods

    /**
     * Creates and return a new tree instance.
     */
    protected Tree createTree() {
        return new Tree("tree");
    }

    /**
     * Build the tree and tree nodes and store the nodes in the session.
     */
    protected Tree buildTree() {
        tree = createTree();

        //Try and load the already stored nodes from the session.
        //If this is the first time we access the page, this method
        //will return null, indicating no nodes is stored.
        TreeNode existingRootNode = loadNodesFromSession();

        if(existingRootNode != null) {
            //OK we had already nodes stored in the session, so no need
            //to rebuild them. We attach the root node and return.
            tree.setRootNode(existingRootNode);

            //By default root node is not displayed in browser. Here we expand it,
            //so that the root's children are visible.
            tree.expand(existingRootNode);
            return tree;
        }

        TreeNode root = createNodes();

        //Attach the root node containing all the other nodes to the tree
        tree.setRootNode(root);

        //By default root node is not displayed in browser. Here we expand it,
        //so that the root's children are visible.
        tree.expand(root);

        //Store the tree nodes in the session so on the next request we do not
        //have to rebuild it. Also because we keep the tree nodes in the session
        //the nodes will keep their state between requests.
        storeNodesInSession(root);

        return tree;
    }

    /**
     * Create the tree nodes. The nodes represents a directory consisting of
     * files and folders as found on an Operating System.
     *
     * @return the root tree node
     */
    protected TreeNode createNodes() {
        
        //Create a node representing the root directory with the specified
        //parameter as the value. Because an id is not specified, a random
        //one will be generated by the node. By default the root node is
        //not rendered by the tree. This can be changed by calling
        //tree.setRootNodeDisplayed(true).
        TreeNode root = new TreeNode("c:");

        //Create a new directory, setting the root directory as its parent. Here
        //we do specify a id as the 2nd argument, so no id is generated.
        TreeNode dev = new TreeNode("dev","1", root);

        //The follwing 2 nodes represent files in the directory, setting the
        //dev node as their parent. Note the false argument to the constructor.
        //This means that the specific node does not support child nodes, and
        //it will be rendered as a leaf icon. If children are supported (the
        //default value) then even  if the node is a leaf, it will still be rendered
        //as a collapsed icon. In the example a default leaf node will be
        //rendered as a directory, and a node that does not support children is
        //rendered as a file.
        new TreeNode("java.pdf", "2", dev, false);
        new TreeNode("ruby.pdf", "3", dev, false);

        //We continue constructing the rest of the tree
        TreeNode programFiles = new TreeNode("program files", "4", root);
        new TreeNode("Adobe", "5", programFiles);

        TreeNode download = new TreeNode("downloads","6", root);

        TreeNode web = new TreeNode("web", "7", download);
        new TreeNode("html.pdf", "8", web, false);
        new TreeNode("css.html", "9", web, false);

        TreeNode databases = new TreeNode("databases", "10", download);
        TreeNode relationalDb = new TreeNode("relational", "11", databases);

        new TreeNode("mysql.html","13", relationalDb, false);
        new TreeNode("oracle.pdf","14", relationalDb, false);
        new TreeNode("postgres","15", relationalDb, false);

        TreeNode objectDb = new TreeNode("object", "12", databases);
        new TreeNode("db4o.html","16", objectDb, false);

        return root;
    }

    /**
     * Return the string under which the nodes are stored in the session.
     * 
     * @return the string under which the nodes are stored in the session
     */
    protected String getSessionKey() {
        return TREE_NODES_SESSION_KEY;
    }

    /**
     * Store the tree nodes in the session
     */
    protected void storeNodesInSession(TreeNode rootNode) {
        if (tree.getRootNode() == null) {
            return;
        }

        getContext().getSession().setAttribute(getSessionKey(), rootNode);
    }

    /**
     * Retrieve the tree nodes from the session if available. Otherwise we return
     * null.
     */
    protected TreeNode loadNodesFromSession() {
        return (TreeNode) getContext().getSession().getAttribute(getSessionKey());
    }
}
