/*
 * #%L
 * Swing JTree check box nodes.
 * %%
 * Copyright (C) 2012 - 2017 Board of Regents of the University of
 * Wisconsin-Madison.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package checkboxJtree;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;

/**
 * A {@link TreeCellEditor} for check box tree nodes.
 */
public class SubCategoryNodeEditor extends AbstractCellEditor implements
        TreeCellEditor {

    private final SubCategoryNodeRenderer renderer = new SubCategoryNodeRenderer();

    private final JTree tree;

    public SubCategoryNodeEditor(final JTree tree) {
        this.tree = tree;
    }

    @Override
    public Object getCellEditorValue() {
        final SubCategoryPanel panel = renderer.getPanel();
        return new SubCategoryNodeData(panel.subCategory);
    }

    @Override
    public boolean isCellEditable(final EventObject event) {
        if (!(event instanceof MouseEvent)) return false;
        final MouseEvent mouseEvent = (MouseEvent) event;

        final TreePath path =
                tree.getPathForLocation(mouseEvent.getX(), mouseEvent.getY());
        if (path == null) return false;

        final Object node = path.getLastPathComponent();
        if (!(node instanceof DefaultMutableTreeNode)) return false;
        final DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) node;

        final Object userObject = treeNode.getUserObject();
        return userObject instanceof SubCategoryNodeData;
    }

    @Override
    public Component getTreeCellEditorComponent(final JTree tree,
                                                final Object value, final boolean selected, final boolean expanded,
                                                final boolean leaf, final int row) {

        final Component editor =
                renderer.getTreeCellRendererComponent(tree, value, true, expanded, leaf,
                        row, true);

        // editor always selected / focused
        final ItemListener itemListener = new ItemListener() {

            @Override
            public void itemStateChanged(final ItemEvent itemEvent) {
                if (stopCellEditing()) {
                    fireEditingStopped();
                }
            }
        };
        if (editor instanceof SubCategoryPanel) {
            final SubCategoryPanel panel = (SubCategoryPanel) editor;
            panel.check.addItemListener(itemListener);
        }

        return editor;
    }
}