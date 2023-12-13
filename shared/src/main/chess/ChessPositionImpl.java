package chess;

import java.util.Objects;

public class ChessPositionImpl implements ChessPosition
{
    private int row;
    private int column;

    public ChessPositionImpl(int row, int column)
    {
        this.row = row;
        this.column = column;
    }

    public boolean validPos() { return row < 8 && row >= 0 && column < 8 && column >= 0; }
    @Override
    public int getRow() { return row; }
    @Override
    public int getColumn() { return column; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPositionImpl that = (ChessPositionImpl) o;
        return row == that.row && column == that.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
}
