package chess;

import java.util.Objects;

public class ChessPositionImpl implements ChessPosition
{
    private final int row;
    private final int col;

    public ChessPositionImpl(int row, int col)
    {
        this.row = row;
        this.col = col;
    }

    public boolean validPos() { return row < 8 && row >= 0 && col < 8 && col >= 0; }
    @Override
    public int getRow() { return row; }
    @Override
    public int getCol() { return col; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPositionImpl that = (ChessPositionImpl) o;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
