# ⚔️ Combat Master

<div align="center">

**Profesyonel GUI menü sistemi, çok dil desteği ve gelişmiş özelliklere sahip Minecraft combat plugin'i**

[![bStats](https://img.shields.io/badge/bStats-28408-brightgreen.svg)](https://bstats.org/plugin/bukkit/Combat-Master/28408)
[![Version](https://img.shields.io/badge/version-v1.0.0-blue.svg)](https://github.com/amhunter1/Combat-Master)
[![Java](https://img.shields.io/badge/Java-17%2B-orange.svg)](https://adoptium.net/)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.20%2B-green.svg)](https://www.spigotmc.org/)

[📥 Download/İndir](https://modrinth.com/plugin/combat-master) • 
[📖 Wiki](https://github.com/amhunter1/Combat-Master/wiki) • 
[💬 Discord](https://discord.com/users/871721944268038175) • 
[🐛 Issues](https://github.com/amhunter1/Combat-Master/issues)

</div>

---

## 🇹🇷 Türkçe

### ✨ Ana Özellikler

#### 🖥️ **Profesyonel GUI Menü Sistemi**
- **Ana Menü**: Combat hub'ı - tüm özelliklere merkezi erişim
- **İstatistik Menüsü**: Detaylı oyuncu performans analizi ve ilerleme takibi  
- **Sıralama Menüsü**: Sayfalı leaderboard sistemi (21 oyuncu/sayfa)
- **Admin Panel**: Kapsamlı yönetici araçları ve sistem monitörü
- **Ayarlar Menüsü**: Detaylı konfigürasyon görüntüleyici
- **Oyuncu Yönetimi**: Online oyuncu yönetim araçları
- **Hızlı Komutlar**: One-click admin işlemleri

#### 🌍 **Çok Dil Desteği**
- **Türkçe (TR)** ve **İngilizce (EN)** tam desteği
- Dinamik dil değişimi (`config.yml` → `language: tr/en`)
- Özelleştirilebilir çeviri dosyaları (`lang/tr.yml`, `lang/en.yml`)
- Fallback sistemi (eksik çevirilerde otomatik TR)

#### ⚡ **Gelişmiş Combat Sistemi**
- Combo seviyelerine göre dinamik ses efektleri
- Real-time action bar mesajları ve renk sistemi
- Gelişmiş combo algılama ve timeout yönetimi
- Mob/player vuruş seçenekleri
- Dünya bazlı etkinleştirme

#### 📊 **Database & Analytics**
- **SQLite** (varsayılan) ve **MySQL** desteği
- **HikariCP** connection pooling
- **bStats** entegrasyonu (Plugin ID: 28408)
- Otomatik veri kaydetme ve yedekleme

### 🚀 Hızlı Başlangıç

#### Gereksinimler
- **Java 17+**
- **Spigot/Paper 1.20+**
- **PlaceholderAPI** (opsiyonel)

#### Kurulum
1. **Plugin'i İndir**: [Latest Release](https://github.com/amhunter1/Combat-Master/releases)
2. **Kur**: JAR dosyasını `plugins/` klasörüne koy
3. **Başlat**: Sunucuyu restart et
4. **Yapılandır**: `plugins/Combat-Master/config.yml` dosyasını düzenle
5. **Dil Seç**: `language: tr` veya `language: en`

### 🎮 Kullanım

#### Temel Komutlar
```bash
/combatmaster              # Ana GUI menüsünü açar
/combatmaster gui          # Ana GUI menüsünü açar  
/combatmaster stats        # İstatistik menüsünü açar
/combatmaster top          # Leaderboard menüsünü açar
/combatmaster reload       # Plugin'i yeniden yükler (admin)
```

#### GUI Navigasyonu
- **Oyuncular**: `/combatmaster` ile ana menüye erişim
- **Adminler**: `combatmaster.admin` yetkisi ile tam erişim
- **Menü İçi**: Sezgisel buton sistemi ve navigasyon
- **Geri/İleri**: Tüm menülerde tutarlı navigasyon

### ⚙️ Konfigürasyon

#### Ana Config (`config.yml`)
```yaml
# Dil Ayarları
language: "tr"  # tr, en

# GUI Ayarları
gui:
  sounds:
    enabled: true
    open: "BLOCK_CHEST_OPEN"
    click: "UI_BUTTON_CLICK"
  performance:
    auto-refresh-interval: 30
    max-open-menus: 50
  appearance:
    use-borders: true
    items-per-page: 21

# Combat Sistemi
sounds:
  enabled: true
  combo-sounds:
    low: {sound: "ENTITY_PLAYER_ATTACK_STRONG", volume: 0.8, pitch: 1.0}
    medium: {sound: "ENTITY_EXPERIENCE_ORB_PICKUP", volume: 1.0, pitch: 1.2}
    high: {sound: "ENTITY_PLAYER_LEVELUP", volume: 1.2, pitch: 1.5}
    epic: {sound: "ENTITY_ENDER_DRAGON_GROWL", volume: 1.5, pitch: 2.0}

combo:
  timeout-seconds: 10
  color-system:
    enabled: true
    colors:
      1-5: "&7"    # Gri
      6-10: "&f"   # Beyaz  
      11-20: "&e"  # Sarı
      21-35: "&6"  # Turuncu
      36-50: "&c"  # Kırmızı
      51-75: "&d"  # Pembe
      76-100: "&5" # Mor
      101+: "&b"   # Açık Mavi

# Database
database:
  type: "sqlite"  # sqlite, mysql
  sqlite:
    file: "combatmaster.db"
```

#### Dil Dosyaları
- **Türkçe**: `plugins/Combat-Master/lang/tr.yml`
- **İngilizce**: `plugins/Combat-Master/lang/en.yml`
- **Özelleştirme**: Dosyaları düzenleyerek metinleri değiştirin

### 🛠️ Admin Araçları

#### GUI Admin Panel
- **Plugin Yönetimi**: Reload, restart, system info
- **Oyuncu Yönetimi**: Online player tracking, combo reset
- **Database Yönetimi**: Backup, statistics, cleanup
- **Hızlı Komutlar**: Toplu işlemler, emergency stop
- **Sistem İzleme**: Memory, performance, metrics

#### İzinler
- `combatmaster.use` (varsayılan: `true`) - Temel kullanım
- `combatmaster.admin` (varsayılan: `op`) - Admin erişimi

### 📈 PlaceholderAPI

```bash
%combatmaster_combo%        # Mevcut combo
%combatmaster_best_combo%   # En iyi combo
%combatmaster_total_hits%   # Toplam hit
%combatmaster_rank%         # Sıralamadaki yer
```

### 💡 İpuçları & SSS

**Q: Menüler açılmıyor?**
A: `combatmaster.use` izninin olduğundan emin olun.

**Q: Dil değişmiyor?**
A: `config.yml`'de `language: tr` veya `language: en` ayarladıktan sonra `/combatmaster reload`

**Q: Sesler çalışmıyor?**
A: `sounds.enabled: true` olduğundan emin olun, ses adları sunucu versiyonuyla uyumlu olmalı.

---

## 🇬🇧 English

### ✨ Key Features

#### 🖥️ **Professional GUI Menu System**
- **Main Menu**: Combat hub with centralized access to all features
- **Stats Menu**: Detailed player performance analysis and progress tracking
- **Leaderboard Menu**: Paginated ranking system (21 players/page)
- **Admin Panel**: Comprehensive management tools and system monitoring
- **Settings Menu**: Detailed configuration viewer
- **Player Management**: Online player management tools
- **Quick Commands**: One-click admin operations

#### 🌍 **Multi-Language Support**
- **Turkish (TR)** and **English (EN)** full support
- Dynamic language switching (`config.yml` → `language: tr/en`)
- Customizable translation files (`lang/tr.yml`, `lang/en.yml`)  
- Fallback system (automatic TR fallback for missing translations)

#### ⚡ **Advanced Combat System**
- Dynamic sound effects based on combo levels
- Real-time action bar messages and color system
- Advanced combo detection and timeout management
- Mob/player hit options
- World-based activation

#### 📊 **Database & Analytics**
- **SQLite** (default) and **MySQL** support
- **HikariCP** connection pooling
- **bStats** integration (Plugin ID: 28408)
- Automatic data saving and backup

### 🚀 Quick Start

#### Requirements
- **Java 17+**
- **Spigot/Paper 1.20+**
- **PlaceholderAPI** (optional)

#### Installation
1. **Download Plugin**: [Latest Release](https://github.com/amhunter1/Combat-Master/releases)
2. **Install**: Place JAR file in `plugins/` folder
3. **Start**: Restart the server
4. **Configure**: Edit `plugins/Combat-Master/config.yml`
5. **Set Language**: `language: tr` or `language: en`

### 🎮 Usage

#### Basic Commands
```bash
/combatmaster              # Opens main GUI menu
/combatmaster gui          # Opens main GUI menu
/combatmaster stats        # Opens stats menu
/combatmaster top          # Opens leaderboard menu
/combatmaster reload       # Reloads plugin (admin)
```

#### GUI Navigation
- **Players**: Access main menu with `/combatmaster`
- **Admins**: Full access with `combatmaster.admin` permission
- **In-Menu**: Intuitive button system and navigation
- **Back/Forward**: Consistent navigation across all menus

### ⚙️ Configuration

#### Main Config (`config.yml`)
```yaml
# Language Settings
language: "en"  # tr, en

# GUI Settings
gui:
  sounds:
    enabled: true
    open: "BLOCK_CHEST_OPEN"
    click: "UI_BUTTON_CLICK"
  performance:
    auto-refresh-interval: 30
    max-open-menus: 50
  appearance:
    use-borders: true
    items-per-page: 21

# Combat System
sounds:
  enabled: true
  combo-sounds:
    low: {sound: "ENTITY_PLAYER_ATTACK_STRONG", volume: 0.8, pitch: 1.0}
    medium: {sound: "ENTITY_EXPERIENCE_ORB_PICKUP", volume: 1.0, pitch: 1.2}
    high: {sound: "ENTITY_PLAYER_LEVELUP", volume: 1.2, pitch: 1.5}
    epic: {sound: "ENTITY_ENDER_DRAGON_GROWL", volume: 1.5, pitch: 2.0}

combo:
  timeout-seconds: 10
  color-system:
    enabled: true
    colors:
      1-5: "&7"    # Gray
      6-10: "&f"   # White
      11-20: "&e"  # Yellow
      21-35: "&6"  # Orange
      36-50: "&c"  # Red
      51-75: "&d"  # Pink
      76-100: "&5" # Purple
      101+: "&b"   # Aqua

# Database
database:
  type: "sqlite"  # sqlite, mysql
  sqlite:
    file: "combatmaster.db"
```

#### Language Files
- **Turkish**: `plugins/Combat-Master/lang/tr.yml`
- **English**: `plugins/Combat-Master/lang/en.yml`
- **Customize**: Edit files to change messages

### 🛠️ Admin Tools

#### GUI Admin Panel
- **Plugin Management**: Reload, restart, system info
- **Player Management**: Online player tracking, combo reset
- **Database Management**: Backup, statistics, cleanup  
- **Quick Commands**: Bulk operations, emergency stop
- **System Monitoring**: Memory, performance, metrics

#### Permissions
- `combatmaster.use` (default: `true`) - Basic usage
- `combatmaster.admin` (default: `op`) - Admin access

### 📈 PlaceholderAPI

```bash
%combatmaster_combo%        # Current combo
%combatmaster_best_combo%   # Best combo
%combatmaster_total_hits%   # Total hits
%combatmaster_rank%         # Rank position
```

### 💡 Tips & FAQ

**Q: Menus not opening?**
A: Ensure you have `combatmaster.use` permission.

**Q: Language not changing?**
A: Set `language: tr` or `language: en` in `config.yml`, then `/combatmaster reload`

**Q: Sounds not working?**
A: Ensure `sounds.enabled: true`, sound names must match your server version.

---

## 🔧 Development

### Project Structure
```
src/main/java/com/melut/combatmaster/
├── gui/                    # GUI Framework
│   ├── BaseGUI.java       # Base menu class
│   ├── MenuManager.java   # Menu management
│   ├── MenuListener.java  # Click handling
│   ├── utils/             # GUI utilities
│   └── menus/             # All menu implementations
├── managers/              # Core managers
│   ├── CombatManager.java # Combat logic
│   ├── ConfigManager.java # Config handling
│   └── LangManager.java   # Language system
├── database/              # Database layer
└── listeners/             # Event listeners

src/main/resources/
├── config.yml            # Main configuration
├── plugin.yml           # Plugin metadata
└── lang/               # Language files
    ├── tr.yml         # Turkish
    └── en.yml         # English
```

### Building
```bash
git clone https://github.com/amhunter1/Combat-Master.git
cd Combat-Master
mvn clean package
```

### Contributing
1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open Pull Request

---

## 📊 Statistics

- **📈 bStats**: [View Plugin Statistics](https://bstats.org/plugin/bukkit/Combat-Master/28408)
- **⭐ GitHub**: [Star the Repository](https://github.com/amhunter1/Combat-Master)
- **🐛 Issues**: [Report Bugs](https://github.com/amhunter1/Combat-Master/issues)

---

## 📞 Support & Contact

<div align="center">

**🔗 Links**
- **Discord**: [Contact Developer](https://discord.com/users/871721944268038175)
- **GitHub**: [Project Repository](https://github.com/amhunter1/Combat-Master)  
- **Download**: [Modrinth Page](https://modrinth.com/plugin/combat-master)

**💝 Support the Project**
- ⭐ Star the repository
- 🐛 Report issues and bugs
- 💡 Suggest new features
- 🤝 Contribute code improvements

</div>

---

## 📄 License

This project is closed-source. Distribution terms belong to the owner. All rights reserved unless stated otherwise.

Bu proje kapalı kaynak kodludur. Dağıtım koşulları proje sahibine aittir. Aksi belirtilmedikçe tüm hakları saklıdır.

---

<div align="center">

**⚔️ Combat Master v1.0.0**

*Made with ❤️ by [Melut](https://github.com/amhunter1)*

</div>
