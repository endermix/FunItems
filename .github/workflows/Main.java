package org.endermix.funitems;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main extends JavaPlugin implements Listener, TabExecutor {

    // Параметры для Fire
    private String fireItemName;
    private Material fireItemType;
    private double fireParticleRadius;
    private int fireNumParticles;
    private double fireParticleSpeed;
    private Sound fireSoundType;
    private float fireSoundVolume;
    private float fireSoundPitch;
    private int fireDuration;

    // Параметры для Dust
    private String dustItemName;
    private Material dustItemType;
    private double dustParticleRadius;
    private int dustNumParticles;
    private double dustParticleSpeed;
    private org.bukkit.Color dustParticleColor;
    private Sound dustSoundType;
    private float dustSoundVolume;
    private float dustSoundPitch;
    private int dustEffectDuration;
    private int dustEffectAmplifier;

    // Параметры для Divine Aura (Божья Аура)
    private String divineAuraItemName;
    private Material divineAuraItemType;
    private double divineAuraParticleRadius;
    private int divineAuraNumParticles;
    private Sound divineAuraSoundType;
    private float divineAuraSoundVolume;
    private float divineAuraSoundPitch;
    private int divineAuraEffectDuration;

    // Параметры для Deorientation
    private String deorientationItemName;
    private Material deorientationItemType;
    private double deorientationParticleRadius;
    private int deorientationNumParticles;
    private double deorientationParticleSpeed;
    private org.bukkit.Color deorientationParticleColor;
    private Sound deorientationSoundType;
    private float deorientationSoundVolume;
    private float deorientationSoundPitch;
    private int deorientationEffectDuration;
    private int deorientationEffectAmplifier;


    @Override
    public void onEnable() {
        // Сохраняем конфигурацию по умолчанию, если она не существует
        saveDefaultConfig();
        // Загружаем параметры конфигурации
        loadConfig();

        // Регистрируем обработчик событий
        getServer().getPluginManager().registerEvents(this, this);

        // Регистрируем команду funitems с обработчиком команд
        if (getCommand("funitems") != null) {
            getCommand("funitems").setExecutor(this);
        }

        // Регистрируем команду funitems reload
        getCommand("funitems_reload").setExecutor(new ReloadCommand(this));
    }

    private void loadConfig() {
        // Загрузка параметров для Fire
        fireItemName = getConfig().getString("fire.item.name", "§cПламя");
        fireItemType = Material.matchMaterial(getConfig().getString("fire.item.type", "BLAZE_ROD"));
        fireParticleRadius = getConfig().getDouble("fire.particle.radius", 5.0);
        fireNumParticles = getConfig().getInt("fire.particle.numParticles", 60);
        fireParticleSpeed = getConfig().getDouble("fire.particle.speed", 0.2);
        fireSoundType = Sound.valueOf(getConfig().getString("fire.sound.type", "ENTITY_BLAZE_SHOOT"));
        fireSoundVolume = (float) getConfig().getDouble("fire.sound.volume", 1.0);
        fireSoundPitch = (float) getConfig().getDouble("fire.sound.pitch", 1.0);
        fireDuration = getConfig().getInt("fire.effect.duration", 100);

        // Загрузка параметров для Dust
        dustItemName = getConfig().getString("dust.item.name", "§fЯвная Пыль");
        dustItemType = Material.matchMaterial(getConfig().getString("dust.item.type", "REDSTONE"));
        dustParticleRadius = getConfig().getDouble("dust.particle.radius", 3.0);
        dustNumParticles = getConfig().getInt("dust.particle.numParticles", 50);
        dustParticleSpeed = getConfig().getDouble("dust.particle.speed", 0.1);
        String[] colorComponents = getConfig().getString("dust.particle.color", "255,0,0").split(",");
        dustParticleColor = org.bukkit.Color.fromRGB(
                Integer.parseInt(colorComponents[0]),
                Integer.parseInt(colorComponents[1]),
                Integer.parseInt(colorComponents[2])
        );
        dustSoundType = Sound.valueOf(getConfig().getString("dust.sound.type", "ENTITY_EXPERIENCE_ORB_PICKUP"));
        dustSoundVolume = (float) getConfig().getDouble("dust.sound.volume", 1.0);
        dustSoundPitch = (float) getConfig().getDouble("dust.sound.pitch", 1.0);
        dustEffectDuration = getConfig().getInt("dust.effect.duration", 100);
        dustEffectAmplifier = getConfig().getInt("dust.effect.amplifier", 1);

        // Загрузка параметров для Divine Aura
        divineAuraItemName = getConfig().getString("divine_aura.item.name", "§bБожья Аура");
        divineAuraItemType = Material.matchMaterial(getConfig().getString("divine_aura.item.type", "PHANTOM_MEMBRANE"));
        divineAuraParticleRadius = getConfig().getDouble("divine_aura.particle.radius", 3.0);
        divineAuraNumParticles = getConfig().getInt("divine_aura.particle.numParticles", 30); // Используется
        divineAuraSoundType = Sound.valueOf(getConfig().getString("divine_aura.sound.type", "ENTITY_EXPERIENCE_ORB_PICKUP"));
        divineAuraSoundVolume = (float) getConfig().getDouble("divine_aura.sound.volume", 1.0);
        divineAuraSoundPitch = (float) getConfig().getDouble("divine_aura.sound.pitch", 1.0);
        divineAuraEffectDuration = getConfig().getInt("divine_aura.effect.duration", 100);

        // Загрузка параметров для Deorientation
        deorientationItemName = getConfig().getString("deorientation.item.name", "§5Дезориентация");
        deorientationItemType = Material.matchMaterial(getConfig().getString("deorientation.item.type", "END_ROD"));
        deorientationParticleRadius = getConfig().getDouble("deorientation.particle.radius", 3.0);
        deorientationNumParticles = getConfig().getInt("deorientation.particle.numParticles", 50);
        deorientationParticleSpeed = getConfig().getDouble("deorientation.particle.speed", 0.1);
        colorComponents = getConfig().getString("deorientation.particle.color", "0,0,255").split(",");
        deorientationParticleColor = org.bukkit.Color.fromRGB(
                Integer.parseInt(colorComponents[0]),
                Integer.parseInt(colorComponents[1]),
                Integer.parseInt(colorComponents[2])
        );
        deorientationSoundType = Sound.valueOf(getConfig().getString("deorientation.sound.type", "ENTITY_ENDER_DRAGON_FLAP"));
        deorientationSoundVolume = (float) getConfig().getDouble("deorientation.sound.volume", 1.0);
        deorientationSoundPitch = (float) getConfig().getDouble("deorientation.sound.pitch", 1.0);
        deorientationEffectDuration = getConfig().getInt("deorientation.effect.duration", 100);
        deorientationEffectAmplifier = getConfig().getInt("deorientation.effect.amplifier", 2);

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("funitems")) {
            if (args.length == 4 && args[0].equalsIgnoreCase("give")) {
                Player target = Bukkit.getPlayer(args[1]);
                if (target != null) {
                    String itemID = args[2];
                    int amount;
                    try {
                        amount = Integer.parseInt(args[3]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(ChatColor.RED + "Количество должно быть числом.");
                        return false;
                    }

                    ItemStack item = null;
                    if (itemID.equalsIgnoreCase("deorientation")) {
                        item = new ItemStack(deorientationItemType, amount);
                        ItemMeta meta = item.getItemMeta();
                        if (meta != null) {
                            meta.setDisplayName(deorientationItemName);
                            item.setItemMeta(meta);
                        }
                    } else if (itemID.equalsIgnoreCase("fire")) {
                        item = new ItemStack(fireItemType, amount);
                        ItemMeta meta = item.getItemMeta();
                        if (meta != null) {
                            meta.setDisplayName(fireItemName);
                            item.setItemMeta(meta);
                        }
                    } else if (itemID.equalsIgnoreCase("dust")) {
                        item = new ItemStack(dustItemType, amount);
                        ItemMeta meta = item.getItemMeta();
                        if (meta != null) {
                            meta.setDisplayName(dustItemName);
                            item.setItemMeta(meta);
                        }
                    } else if (itemID.equalsIgnoreCase("divine_aura")) {
                        item = new ItemStack(divineAuraItemType, amount);
                        ItemMeta meta = item.getItemMeta();
                        if (meta != null) {
                            meta.setDisplayName(divineAuraItemName);
                            item.setItemMeta(meta);
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Неизвестный предмет");
                        return false;
                    }

                    if (item != null) {
                        target.getInventory().addItem(item);
                        sender.sendMessage("§fВыдано §7" + amount + " " + item.getItemMeta().getDisplayName() + "§f игроку " + target.getName());
                    }
                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED + "Игрок не найден.");
                    return false;
                }
            }
        }
        return false;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() == fireItemType && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null && fireItemName.equals(meta.getDisplayName())) {
                if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    event.setCancelled(true);

                    // Уменьшаем количество предметов или удаляем их
                    if (item.getAmount() > 1) {
                        item.setAmount(item.getAmount() - 1);
                    } else {
                        player.getInventory().removeItem(item);
                    }

                    // Создаем частички
                    createParticleCircle(player);

                    // Воспроизводим звук
                    player.getWorld().playSound(player.getLocation(), fireSoundType, fireSoundVolume, fireSoundPitch);

                    // Поджигаем игрока
                    player.setFireTicks(fireDuration);

                    // Поджигаем игроков в радиусе 5 блоков
                    for (Player nearbyPlayer : Bukkit.getOnlinePlayers()) {
                        if (nearbyPlayer.getLocation().distance(player.getLocation()) <= 5 && !nearbyPlayer.equals(player)) {
                            nearbyPlayer.setFireTicks(fireDuration);
                        }
                    }
                }
            }
        } else if (item.getType() == dustItemType && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null && dustItemName.equals(meta.getDisplayName())) {
                if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    event.setCancelled(true);

                    // Уменьшаем количество предметов или удаляем их
                    if (item.getAmount() > 1) {
                        item.setAmount(item.getAmount() - 1);
                    } else {
                        player.getInventory().removeItem(item);
                    }

                    // Создаем частички
                    createDustParticleCircle(player);

                    // Воспроизводим звук
                    player.getWorld().playSound(player.getLocation(), dustSoundType, dustSoundVolume, dustSoundPitch);

                    // Применяем эффекты к игроку
                    player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, dustEffectDuration, 1));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, dustEffectDuration, dustEffectAmplifier));

                    // Применяем эффекты к игрокам в радиусе 5 блоков
                    for (Player nearbyPlayer : Bukkit.getOnlinePlayers()) {
                        if (nearbyPlayer.getLocation().distance(player.getLocation()) <= 5 && !nearbyPlayer.equals(player)) {
                            nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, dustEffectDuration, 1));
                            nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, dustEffectDuration, dustEffectAmplifier));
                        }
                    }
                }
            }
        } else if (item.getType() == divineAuraItemType && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null && divineAuraItemName.equals(meta.getDisplayName())) {
                if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    event.setCancelled(true);

                    // Уменьшаем количество предметов или удаляем их
                    if (item.getAmount() > 1) {
                        item.setAmount(item.getAmount() - 1);
                    } else {
                        player.getInventory().removeItem(item);
                    }

                    // Лечим самого игрока
                    player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());

                    // Создаем частички
                    createDivineAuraParticleCircle(player);

                    // Воспроизводим звук
                    player.getWorld().playSound(player.getLocation(), divineAuraSoundType, divineAuraSoundVolume, divineAuraSoundPitch);

                    // Лечим игроков в радиусе 3 блоков
                    for (Player nearbyPlayer : Bukkit.getOnlinePlayers()) {
                        if (nearbyPlayer.getLocation().distance(player.getLocation()) <= 3) {
                            nearbyPlayer.setHealth(nearbyPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                        }
                    }
                }
            }
        } else if (item.getType() == deorientationItemType && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null && deorientationItemName.equals(meta.getDisplayName())) {
                if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    event.setCancelled(true);

                    // Уменьшаем количество предметов или удаляем их
                    if (item.getAmount() > 1) {
                        item.setAmount(item.getAmount() - 1);
                    } else {
                        player.getInventory().removeItem(item);
                    }

                    // Создаем частички
                    createDeorientationParticleCircle(player);

                    // Воспроизводим звук
                    player.getWorld().playSound(player.getLocation(), deorientationSoundType, deorientationSoundVolume, deorientationSoundPitch);

                    // Применяем эффекты к игроку
                    player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, deorientationEffectDuration, deorientationEffectAmplifier));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, deorientationEffectDuration, deorientationEffectAmplifier));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, deorientationEffectDuration, deorientationEffectAmplifier));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, deorientationEffectDuration, deorientationEffectAmplifier));

                    // Применяем эффекты к игрокам в радиусе 5 блоков
                    for (Player nearbyPlayer : Bukkit.getOnlinePlayers()) {
                        if (nearbyPlayer.getLocation().distance(player.getLocation()) <= 5 && !nearbyPlayer.equals(player)) {
                            nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, deorientationEffectDuration, deorientationEffectAmplifier));
                            nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, deorientationEffectDuration, deorientationEffectAmplifier));
                            nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, deorientationEffectDuration, deorientationEffectAmplifier));
                            nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, deorientationEffectDuration, deorientationEffectAmplifier));
                        }
                    }
                }
            }
        }
    }

    private void createParticleCircle(Player player) {
        new BukkitRunnable() {
            final double radius = fireParticleRadius;
            final int numParticles = fireNumParticles;
            final double speed = fireParticleSpeed;
            final Location center = player.getLocation().clone().add(0, 1, 0);
            double progress = 0;

            @Override
            public void run() {
                progress += speed;

                if (progress > radius) {
                    progress = radius;
                    cancel();
                }

                for (int i = 0; i < numParticles; i++) {
                    double angle = 2 * Math.PI * i / numParticles;
                    double x = progress * Math.cos(angle);
                    double z = progress * Math.sin(angle);
                    Location particleLocation = center.clone().add(x, 0, z);
                    player.getWorld().spawnParticle(Particle.FLAME, particleLocation, 0);
                }
            }
        }.runTaskTimer(this, 0L, 1L);
    }

    private void createDustParticleCircle(Player player) {
        new BukkitRunnable() {
            final double radius = dustParticleRadius;
            final int numParticles = dustNumParticles;
            final double speed = dustParticleSpeed;
            final Location center = player.getLocation().clone().add(0, 1, 0);
            double progress = 0;

            @Override
            public void run() {
                progress += speed;

                if (progress > radius) {
                    progress = radius;
                    cancel();
                }

                for (int i = 0; i < numParticles; i++) {
                    double angle = 2 * Math.PI * i / numParticles;
                    double x = progress * Math.cos(angle);
                    double z = progress * Math.sin(angle);
                    Location particleLocation = center.clone().add(x, 0, z);
                    player.getWorld().spawnParticle(Particle.REDSTONE, particleLocation, 1, new Particle.DustOptions(Color.fromRGB(255, 255, 255), 1));
                }
            }
        }.runTaskTimer(this, 0L, 1L);
    }

    private void createDivineAuraParticleCircle(Player player) {
        final int numParticles = divineAuraNumParticles; // Количество частиц из конфигурации
        final double radius = divineAuraParticleRadius; // Радиус круга из конфигурации
        final Location center = player.getLocation().clone().add(0, 1, 0); // Центр круга немного выше игрока

        // Создаем задачу для частиц
        BukkitRunnable particleTask = new BukkitRunnable() {
            private int tickCount = 0;
            @Override
            public void run() {
                if (tickCount >= 20) { // 30 тиков = 3 секунды (0.1 секунда на тик)
                    cancel(); // Останавливаем задачу после 3 секунд
                }
                tickCount++;

                // Создаем частички
                for (int i = 0; i < numParticles; i++) {
                    double angle = 2 * Math.PI * i / numParticles;
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    Location particleLocation = center.clone().add(x, 0, z);
                    player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, particleLocation, 1);
                }
            }
        };

        // Запускаем задачу для частиц каждые 0.1 секунды
        particleTask.runTaskTimer(this, 0L, 2L);

        // Воспроизводим звук один раз
        player.getWorld().playSound(player.getLocation(), divineAuraSoundType, divineAuraSoundVolume, divineAuraSoundPitch);
    }

    private void createDeorientationParticleCircle(Player player) {
        new BukkitRunnable() {
            final double radius = deorientationParticleRadius;
            final int numParticles = deorientationNumParticles;
            final double speed = deorientationParticleSpeed;
            final Location center = player.getLocation().clone().add(0, 1, 0);
            double progress = 0;

            @Override
            public void run() {
                progress += speed;

                if (progress > radius) {
                    progress = radius;
                    cancel();
                }

                for (int i = 0; i < numParticles; i++) {
                    double angle = 2 * Math.PI * i / numParticles;
                    double x = progress * Math.cos(angle);
                    double z = progress * Math.sin(angle);
                    Location particleLocation = center.clone().add(x, 0, z);
                    player.getWorld().spawnParticle(Particle.REDSTONE, particleLocation, 1, new Particle.DustOptions(deorientationParticleColor, 1));
                }
            }
        }.runTaskTimer(this, 0L, 1L);
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("funitems")) {
            if (args.length == 1) {
                return Collections.singletonList("give");
            } else if (args.length == 2) {
                return null; // Показать список онлайн игроков
            } else if (args.length == 3) {
                List<String> items = new ArrayList<>();
                items.add("fire");
                items.add("dust");
                items.add("divine_aura");
                items.add("deorientation"); // Добавляем новый тип предмета
                return items;
            }
        }
        return Collections.emptyList();
    }
}
