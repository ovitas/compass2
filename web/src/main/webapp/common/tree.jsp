<%@ include file="/common/taglibs.jsp" %>
<!--
tree.jsp
-->

<script type="text/javascript">
Ext.onReady(function(){



    // shorthand
    var Tree = Ext.tree;
	
	var tree = new Tree.TreePanel({
        el: 'tree-div',
        useArrows: true,
        autoScroll: true,
        animate: true,
        autoHeight: true,
        containerScroll: true,
        rootVisible: false, 
        loader: new Tree.TreeLoader()
    });



    // set the root node
    var root = new Tree.TreeNode({
        text: 'Results',
        draggable:false,
        id:'source'
    });


    tree.setRootNode(root);
    
    // json data describing the tree
	var json = ${treeJson};
 
    for(var i = 0, len = json.length; i < len; i++) {   
		root.appendChild(tree.getLoader().createNode(json[i]));
    }
	
    tree.render();
    root.expand(); 
});

</script>
<div id="tree-div"></div>

<!--
tree.jsp END
-->
