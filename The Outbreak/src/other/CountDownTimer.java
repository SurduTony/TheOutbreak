package other;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import Main.*;
import Entities.*;
import Menu.*;

public class CountDownTimer {
    public Timer timer;
    public String timerString;
    public int minute, second;
    public DecimalFormat dFormat;

    Soldier soldier;

    public CountDownTimer(Game game, Soldier soldier) {
        this.soldier = soldier;

        timerString = "Start";

        dFormat = new DecimalFormat("00");

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int second = getSecond();
                int minute = getMinute();
                second--;
                String ddSecond = dFormat.format(second);
                String ddMinute = dFormat.format(minute);
                timerString = ddMinute + ":" + ddSecond;

                if (second == -1) {
                    second = 59;
                    minute--;
                    ddSecond = dFormat.format(second);
                    ddMinute = dFormat.format(minute);
                    timerString = ddMinute + ":" + ddSecond;
                }

                setMinute(minute);
                setSecond(second);

                // play helicopter sound at second 10
                if (minute == 0 && second == 10) {
                    game.playSoundEffect(6);
                }

                // level finished
                if (minute == 0 && second == 0) {
                    timer.stop();
                    game.menuManager.setState(MenuState.levelFinished);
                    game.inMenu = true;

                    if (soldier.vaccinesCollected == soldier.vaccinesToCollect) {
                        game.won = true;
                        soldier.totalKills += soldier.kills;
                        game.playSoundEffect(10);
                    }
                    else {
                        game.playSoundEffect(11);
                        game.won = false;
                    }
                }
            }
        });
    }

    public void start() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    public void set(int minute, int second) {
        timerString = "Start";
        this.minute = minute;
        this.second = second;
    }

    public String getTimerString() {
        return timerString;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }
}
