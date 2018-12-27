package by.alexdem.tictactoe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Game game;
    private TableLayout gameBoardLayout;
    private TextView statusView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameBoardLayout = findViewById(R.id.gameBoardLayout);
        statusView = findViewById(R.id.statusTextView);
        setContentView(R.layout.activity_main);
        TableLayout gameBoardLayout = findViewById(R.id.gameBoardLayout);
        TextView statusView = findViewById(R.id.statusTextView);
        if(savedInstanceState != null) {
            game = new Game(gameBoardLayout, statusView, new Player(savedInstanceState.getChar("firstPlayer")), new Player(savedInstanceState.getChar("secondPlayer")), findViewById(R.id.refreshBtn));
            game.setStateFromBundle(savedInstanceState);
        } else {
            game = new Game(gameBoardLayout, statusView, new Player('X'), new Player('O'), findViewById(R.id.refreshBtn));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putChar("firstPlayer", game.getFirstPlayer().getSymbol());
        state.putChar("secondPlayer", game.getSecondPlayer().getSymbol());
        state.putBoolean("active", game.getActivePlayer() == game.getFirstPlayer());
        state.putSerializable("board", game.getBoard());
        state.putInt("strokeNum", game.getStrokeNum());
        state.putBoolean("isGameEnd", game.isGameEnd());
    }
}
