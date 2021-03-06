package net.acticraft.pixelcategorysg.GameManager;

import net.acticraft.pixelcategorysg.PixelCategorySG;
import net.acticraft.pixelcategorysg.Tasks.DeathmatchCountDownTask;
import net.acticraft.pixelcategorysg.Tasks.GameStartCountdownTask;
import net.acticraft.pixelcategorysg.Tasks.WonTimer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GameManager {
    private final PixelCategorySG plugin;
    private GameState gameState = GameState.LOBBY;

    private final BlockManager blockManager;
    private final PlayerManager playerManager;


    private GameStartCountdownTask gameStartCountdownTask;
    private DeathmatchCountDownTask deathmatchCountDownTask;

    private WonTimer wonTimer;


    public GameManager(PixelCategorySG plugin) {
        this.plugin = plugin;

        this.blockManager = new BlockManager(this);
        this.playerManager = new PlayerManager(this);


    }

    public void setGameState(GameState gameState) {
        if (this.gameState == GameState.ACTIVE && gameState == GameState.STARTING) return;
        if (this.gameState == gameState) return;


        this.gameState = gameState;


        switch (gameState) {
            case ACTIVE:
                if (this.gameStartCountdownTask != null) this.gameStartCountdownTask.cancel();
                this.deathmatchCountDownTask = new DeathmatchCountDownTask(this);
                this.deathmatchCountDownTask.runTaskTimer(plugin, 0, 20);

                break;

            case LOBBY:
                if (this.gameStartCountdownTask != null) this.gameStartCountdownTask.cancel();


                break;
            case STARTING:
                // start countdown task
                // teleport players
                // clear inventories
                // etc
                this.gameStartCountdownTask = new GameStartCountdownTask(this);
                this.gameStartCountdownTask.runTaskTimer(plugin, 0, 20);
                break;

            case PREDEATHMATCH:
                break;

            case WON:
                if (this.wonTimer != null) this.wonTimer.cancel();
                this.wonTimer = new WonTimer(this);
                this.wonTimer.runTaskTimer(plugin, 0, 20);

                break;

        }
    }

    public void cleanup() {

    }

    public BlockManager getBlockManager() {
        return blockManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }


    public GameState getGameState() {
        return gameState;


    }


    public static void EndGame() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage("ending");
        }

        int time = 10; // seconds :)
        Bukkit.getScheduler().scheduleSyncDelayedTask(PixelCategorySG.getInstance(), new Runnable() {
            @Override
            public void run() {
                Bukkit.getServer().shutdown();
            }
        }, 20L*time); // 20 * seconds 20 is 20 ticks 1s
    }
}