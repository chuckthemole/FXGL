/*
 * FXGL - JavaFX Game Library. The MIT License (MIT).
 * Copyright (c) AlmasB (almaslvl@gmail.com).
 * See LICENSE for details.
 */

package com.almasb.fxgl.pathfinding.astar;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.component.Required;
import com.almasb.fxgl.pathfinding.CellMoveComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
@Required(CellMoveComponent.class)
public final class AStarMoveComponent extends Component {

    private CellMoveComponent moveComponent;

    private AStarPathfinder pathfinder;
    private int cellWidth;
    private int cellHeight;

    private List<AStarCell> path = new ArrayList<>();

    /**
     * Cell width and height are required to compute the cell position of the entity to
     * which this component is attached.
     */
    public AStarMoveComponent(AStarPathfinder pathfinder, int cellWidth, int cellHeight) {
        this.pathfinder = pathfinder;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
    }

    public int getCellWidth() {
        return cellWidth;
    }

    public int getCellHeight() {
        return cellHeight;
    }

    public void setCellWidth(int cellWidth) {
        this.cellWidth = cellWidth;
    }

    public void setCellHeight(int cellHeight) {
        this.cellHeight = cellHeight;
    }

    public boolean isMoving() {
        return moveComponent.isMoving();
    }

    public boolean isPathEmpty() {
        return path.isEmpty();
    }

    public AStarGrid getGrid() {
        return pathfinder.getGrid();
    }

    public void moveToCell(AStarCell cell) {
        moveToCell(cell.getX(), cell.getY());
    }

    /**
     * Entity's center is used to position it in the cell.
     */
    public void moveToCell(int x, int y) {
        var center = entity.getCenter();
        int startX = (int) (center.getX() / cellWidth);
        int startY = (int) (center.getY() / cellHeight);

        moveToCell(startX, startY, x, y);
    }

    /**
     * Entity's center is used to position it in the cell.
     * This can be used to explicitly specify the start X and Y of the entity.
     */
    public void moveToCell(int startX, int startY, int targetX, int targetY) {
        path = pathfinder.findPath(startX, startY, targetX, targetY);
    }

    @Override
    public void onUpdate(double tpf) {
        if (path.isEmpty() || moveComponent.isMoving())
            return;

        var next = path.remove(0);

        moveComponent.moveToCell(next.getX(), next.getY());
    }
}
