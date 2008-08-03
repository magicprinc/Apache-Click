package net.sf.click.examples.page.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sf.click.ActionListener;
import net.sf.click.Context;
import net.sf.click.Control;
import net.sf.click.control.Checkbox;
import net.sf.click.control.FieldSet;
import net.sf.click.control.Form;
import net.sf.click.control.Reset;
import net.sf.click.control.Submit;
import net.sf.click.examples.page.BorderPage;
import net.sf.click.extras.tree.CheckboxTree;
import net.sf.click.extras.tree.Tree;
import net.sf.click.extras.tree.TreeListener;
import net.sf.click.extras.tree.TreeNode;

/**
 * Example usage of the {@link CheckboxTree} control.
 *
 * @author Bob Schellink
 */
public class CheckboxTreePage extends BorderPage implements TreeListener {

    public static final String TREE_NODES_SESSION_KEY = "checkboxTreeNodes";

    private CheckboxTree tree;
    private Form form;
    private Submit okSubmit;
    private Reset resetBtn;

    // --------------------------------------------------------- Event Handlers

    /**
     * @see net.sf.click.Page#onInit()
     */
    public void onInit() {
        super.onInit();

        // The checkbox tree needs to be placed inside a form so all the
        // checkbox values can be submitted to the server when we submit
        // the form.
        form = new CheckboxTreeForm("form");

        //Create the tree and tree model and add it to the page
        tree = buildTree();
        tree.addListener(this);

        // Add the tree to the form.
        form.add(tree);

        okSubmit = new Submit("select", "Select");
        okSubmit.setActionListener(new ActionListener() {
            public boolean onAction(Control source) {
                return onSelectClick();
            }
        });
        resetBtn = new Reset("reset", "Reset");
        form.add(okSubmit);
        form.add(resetBtn);

        addControl(form);

        //Build the options user interface for users to interactively
        //change the tree values.
        buildOptionsUI();
    }

    /**
     * Called when user clicks on submit
     */
    public boolean onSelectClick() {
        tree.bindSelectOrDeselectValues();
        return true;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Creates and return a new tree instance.
     */
    public CheckboxTree createTree() {
        return new CheckboxTree("checkboxTree" );
    }

    /**
     * Build the tree model and stores it in the session.
     */
    public CheckboxTree buildTree() {
        tree = createTree();

        //Try and load the already stored nodes from the session.
        //If this is the first time we access the page, this method
        //will return null, indicating no nodes is stored.
        TreeNode existingRootNode = loadNodesFromSession();

        if(existingRootNode != null) {
            //OK we had already nodes stored in the session, so no need
            //to rebuild them. We attach the root node and return.
            tree.setRootNode(existingRootNode);

            return tree;
        }

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
        new TreeNode("mysql.html","11",databases, false);
        new TreeNode("oracle.pdf","12",databases, false);
        new TreeNode("postgres","13",databases, false);

        //Attach the root node containing all the other nodes to the tree
        tree.setRootNode(root);

        //By default root node is not displayed in browser. Here we expand it,
        //so that the root's children are visible.
        expandRoot();

        //Store the tree nodes in the session so on the next request we do not
        //have to rebuild it. Also because we keep the tree nodes in the session
        //the nodes will keep their state between requests.
        storeNodesInSession(root);

        return tree;
    }

    // Custom form that handles the processing of the tree
    class CheckboxTreeForm extends Form {

        public CheckboxTreeForm(String name) {
            super(name);
        }

        // Add check if Tree node is expanded or collapsed so the Tree can
        // be processed
        public boolean onProcess() {
            boolean wasTreeExpanded = getContext().getRequestParameter(Tree.EXPAND_TREE_NODE_PARAM) != null;
            // If tree was expanded or collapsed, the Tree should be processed
            if (wasTreeExpanded) {
                tree.onProcess();
                return true;
            } else {
                // Perform normal Form processing
                return super.onProcess();
            }
        }
    }

    // -------------------------------------------------- TreeListener Support

    /**
     * This method, which implements TreeListener, is called when a node is selected
     */
    public void nodeSelected(Tree tree, TreeNode node, Context context, boolean oldValue) {
        List list = getModelValue("selected");
        list.add(node);
    }

    /**
     * This method, which implements TreeListener, is called when a node is deselected
     */
    public void nodeDeselected(Tree tree, TreeNode node, Context context, boolean oldValue) {
        List list = getModelValue("deselected");
        list.add(node);
    }

    /**
     * This method, which implements TreeListener, is called when a node is expanded
     */
    public void nodeExpanded(Tree tree, TreeNode node, Context context, boolean oldValue) {
        List list = getModelValue("expanded");
        list.add(node);
    }

    /**
     * This method, which implements TreeListener, is called when a node is collapsed
     */
    public void nodeCollapsed(Tree tree, TreeNode node, Context context, boolean oldValue) {
        List list = getModelValue("collapsed");
        list.add(node);
    }

    /**
     * Returns the value for the specified name from the page's model.
     * If a value is not found, a new ArrayList is created and added to the model.
     */
    private List getModelValue(String name) {
        List list = (List) getModel().get(name);
        if(list == null) {
            list = new ArrayList();
            addModel(name, list);
        }
        return list;
    }

    // ------------------------------------------------------------------- NOTE
    //The code below is not specific to tree control usage.

    private Form optionsForm;
    private Checkbox jsEnabled = new Checkbox("jsEnabled", "JavaScript Enabled");
    private Checkbox rootNodeDisplayed = new Checkbox("rootNodeDisplayed", "Display root node");
    private Checkbox selectChildNodes = new Checkbox("selectChildNodes", "Select child nodes");
    private Submit applyOptions = new Submit("apply", "Apply Options", this, "onApplyOptionsClick");

    /**
     * Called when user submits the options form.
     */
    public boolean onApplyOptionsClick() {
        //Reset the tree and nodes to default values
        resetTree();

        //Store the users options in the session
        CheckboxTreeOptions options = new CheckboxTreeOptions();
        options.javascriptEnabled = jsEnabled.isChecked();
        options.rootNodeDisplayed = rootNodeDisplayed.isChecked();
        options.selectChildNodes = selectChildNodes.isChecked();
        setSessionObject(options);

        //Apply users new options
        applyOptions();

        return true;
    }

    /**
     * Builds the user interface for users to change the tree options interactively.
     */
    public void buildOptionsUI() {
        //Form to handle user selected options
        optionsForm = new Form("optionForm");
        FieldSet fieldSet = new FieldSet("options", "Form Options");
        fieldSet.add(jsEnabled);
        fieldSet.add(rootNodeDisplayed);
        fieldSet.add(selectChildNodes);
        optionsForm.add(fieldSet);

        addControl(optionsForm);
        optionsForm.add(applyOptions);

        //Apply users existing options
        applyOptions();
    }

    /** Form options holder. */
    public static class CheckboxTreeOptions implements Serializable {
        private static final long serialVersionUID = 1L;
        boolean javascriptEnabled= false;
        boolean rootNodeDisplayed = false;
        boolean selectChildNodes = false;
    }

    /**
     * Reset the tree to initial state
     */
    private void resetTree() {
        // Remove any Session entries made by Tree
        tree.cleanupSession();

        //Temporarily disable notification to any tree listeners while we reset the tree
        tree.setNotifyListeners(false);

        //Clear all current selections so the tree can start afresh with new options enabled
        tree.deselectAll();
        tree.collapseAll();

        //Enable notification to any tree listeners again
        tree.setNotifyListeners(true);

        if(!rootNodeDisplayed.isChecked()) {
            //If rootNodeDisplayed is false, the root node is not displayed in browser. Here we
            //expand it,  so that the root's children are visible.
            expandRoot();
        }
    }

    /**
     * Store the tree nodes in the session
     */
    private void storeNodesInSession(TreeNode rootNode) {
        if (tree.getRootNode() == null) {
            return;
        }

        getContext().getSession().setAttribute(TREE_NODES_SESSION_KEY, rootNode);
    }

    /**
     * Retrieve the tree nodes from the session if available. Otherwise we return
     * null.
     */
    private TreeNode loadNodesFromSession() {
        return (TreeNode) getContext().getSession().getAttribute(TREE_NODES_SESSION_KEY);
    }

    /**
     * Enable javascript so that there are no round trips to server
     * when expand and collapsing nodes.
     */
    private void enableJavascript(boolean enable) {
        //This call enables the client side javascript functionality
        tree.setJavascriptEnabled(enable);
    }

    /**
     * Applies the user selected options to the tree like enabling javascript,
     * displaying the root node etc.
     */
    private void applyOptions() {

        //We retrieve our stored options from the session and set the controls state
        CheckboxTreeOptions options = (CheckboxTreeOptions) getSessionObject(CheckboxTreeOptions.class);
        jsEnabled.setChecked(options.javascriptEnabled);
        rootNodeDisplayed.setChecked(options.rootNodeDisplayed);
        selectChildNodes.setChecked(options.selectChildNodes);

        //Enable or disable javascript functionality based on users current option
        enableJavascript(options.javascriptEnabled);

        //Indicates if we want to display the root node or not.
        tree.setRootNodeDisplayed(options.rootNodeDisplayed);

        //Indicates if the tree should recursively select child nodes.
        tree.setSelectChildNodes(options.selectChildNodes);
    }

    /**
     * Expand the root but do not notify any tree listeners of the change.
     */
    private void expandRoot() {
        //Temporarily disable notification to any tree listeners while we expand the root node
        tree.setNotifyListeners(false);

        tree.expand(tree.getRootNode());

        //Enable notification to any tree listeners again
        tree.setNotifyListeners(true);
    }
}
