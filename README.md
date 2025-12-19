<div align="center">

# ⚔️ Combat Master

**Minecraft için gelişmiş combat tracking ve istatistik sistemi**

[![Version](https://img.shields.io/badge/version-v2.0.0-blue.svg)](https://github.com/amhunter1/Combat-Master)
[![bStats](https://img.shields.io/badge/bStats-28408-brightgreen.svg)](https://bstats.org/plugin/bukkit/Combat-Master/28408)
[![Java](https://img.shields.io/badge/Java-17%2B-orange.svg)](https://adoptium.net/)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.20%2B-green.svg)](https://www.spigotmc.org/)
[![License](https://img.shields.io/badge/license-Closed_Source-red.svg)](./LICENSE)

[📥 **İndir**](https://modrinth.com/plugin/combat-master) • [📖 **Wiki**](https://github.com/amhunter1/Combat-Master/wiki) • [💬 **Discord**](https://discord.com/users/871721944268038175) • [🐛 **Issues**](https://github.com/amhunter1/Combat-Master/issues)

</div>

---

## 🌟 Öne Çıkan Özellikler

<table>
<tr>
<td>

### 🎨 **Modern GUI Sistemi**
- Tamamen özelleştirilebilir arayüzler
- Akıcı menü geçişleri
- Responsive tasarım
- Çoklu sayfa desteği

</td>
<td>

### 🏆 **Combo Sistemi**
- Gerçek zamanlı combo takibi
- Seviye bazlı renk kodları
- Ses efektleri ve animasyonlar
- Otomatik reset sistemi

</td>
</tr>
<tr>
<td>

### 📊 **İstatistik Analizi**
- Detaylı performans metrikleri
- Sıralama sistemleri
- İlerleme takibi
- Karşılaştırmalı analizler

</td>
<td>

### ⚙️ **Admin Araçları**
- Kapsamlı yönetim paneli
- Toplu işlem desteği
- Sistem monitörü
- Otomatik backup

</td>
</tr>
</table>

---

## 🚀 Hızlı Başlangıç

### 📋 **Gereksinimler**
- **Java**: 17 veya üzeri
- **Sunucu**: Spigot/Paper 1.20+
- **RAM**: En az 512MB (önerilen: 1GB+)
- **PlaceholderAPI**: İsteğe bağlı

### ⚡ **Kurulum**

1. **Plugin'i İndirin**
   ```bash
   wget https://github.com/amhunter1/Combat-Master/releases/latest/Combat-Master-v2.0.0.jar
   ```

2. **Sunucuya Kurun**
   ```bash
   mv Combat-Master-v2.0.0.jar plugins/
   ```

3. **Sunucuyu Başlatın**
   ```bash
   # Otomatik konfigürasyon oluşturulacak
   java -jar spigot.jar
   ```

4. **Ayarları Yapın**
   ```yaml
   # plugins/Combat-Master/config.yml
   language: "tr"  # Türkçe için
   enabled-worlds: []  # Tüm dünyalar için boş bırakın
   ```

---

## 🎮 Ana Sistemler

### 🖥️ **GUI Menü Sistemi**

Combat Master'ın kalbi olan GUI sistemi, kullanıcıların tüm özelliklere kolay erişim sağlamasını mümkün kılar:

<details>
<summary><strong>📱 Ana Menü Hub</strong></summary>

- **Merkezi Kontrol**: Tüm özelliklere tek noktadan erişim
- **Dinamik İçerik**: Gerçek zamanlı veri güncellemeleri  
- **Kullanıcı Dostu**: Sezgisel navigasyon sistemi
- **Hızlı Erişim**: Favorilerinizi kaydedin

```bash
/combatmaster        # Ana menüyü açar
/cm                  # Kısa versiyon
```

</details>

<details>
<summary><strong>📈 İstatistik Paneli</strong></summary>

- **Performans Metrikleri**: Combo, hit, seviye analizi
- **Görsel Grafikler**: İlerleme çubukları ve renk kodları
- **Karşılaştırma**: Diğer oyuncularla performans kıyası
- **Geçmiş Veriler**: Zaman bazlı istatistik görüntüleme

**Özellikler:**
- ⚔️ En İyi Combo Skoru
- 💎 Toplam Hit Sayısı  
- 📊 Seviye ve İlerleme
- 🏅 Sıralama Pozisyonu

</details>

<details>
<summary><strong>🏆 Liderlik Tablosu</strong></summary>

- **Sayfalı Sistem**: 21 oyuncu/sayfa
- **Real-time Updates**: Canlı sıralama güncellemeleri
- **Filtreleme**: Farklı kategorilerde sıralama
- **Ödül Sistemi**: Top 3 için özel efektler

**Kategoriler:**
- 🥇 En İyi Combo
- 🎯 Toplam Hit
- ⚡ Aktif Oyuncular

</details>

### ⚔️ **Combat Sistemi**

Gelişmiş combat tracking sistemi ile oyuncu performansını detaylı şekilde takip edin:

<details>
<summary><strong>🎯 Combo Mekanikleri</strong></summary>

**Combo Hesaplama:**
```java
// Her başarılı hit combo'yu artırır
hit -> combo++

// Belirli süre sonra reset
timeout(10s) -> combo = 0

// Hasar alma durumunda reset
damage_taken -> combo = 0
```

**Combo Seviyeleri:**
- 🟫 **1-5**: Başlangıç (Gri)
- ⚪ **6-10**: Gelişen (Beyaz)
- 🟡 **11-20**: İyi (Sarı)  
- 🟠 **21-35**: Harika (Turuncu)
- 🔴 **36-50**: Mükemmel (Kırmızı)
- 🟣 **51-75**: Efsanevi (Mor)
- 🔵 **76-100**: Destansı (Mavi)
- 🌟 **100+**: Tanrısal (Gökkuşağı)

</details>

<details>
<summary><strong>🔊 Ses ve Efekt Sistemi</strong></summary>

**Dinamik Ses Efektleri:**
- **Düşük Combo (1-10)**: `ENTITY_PLAYER_ATTACK_STRONG`
- **Orta Combo (11-25)**: `ENTITY_EXPERIENCE_ORB_PICKUP`  
- **Yüksek Combo (26-50)**: `ENTITY_PLAYER_LEVELUP`
- **Epic Combo (51+)**: `ENTITY_ENDER_DRAGON_GROWL`

**Action Bar Mesajları:**
```
⚔️ Combo: 25 | 🎯 Hit: 1,337 | 🏅 Sıra: #3
```

</details>

<details>
<summary><strong>🌍 Dünya Yönetimi</strong></summary>

**Esnek Aktivasyon:**
```yaml
enabled-worlds:
  - "world"      # Ana dünya
  - "pvp_arena"  # PvP arenası
  # Boş liste = tüm dünyalar aktif
```

**Özel Ayarlar:**
- Mob vuruşları dahil/hariç
- Kritik vuruş zorunluluğu
- Yerden vuruş kontrolü
- Oyuncu vs oyuncu sınırları

</details>

### 🛠️ **Admin Yönetim Sistemi**

Güçlü admin araçları ile sunucunuzu tam kontrol altında tutun:

<details>
<summary><strong>👥 Oyuncu Yönetimi</strong></summary>

**Canlı Oyuncu Takibi:**
- 📊 Real-time istatistikler
- ⚡ Aktif combo durumları  
- 🎮 Oyuncu profil analizi
- 🔧 Tek tık combo reset

**Toplu İşlemler:**
```bash
# Tüm combo'ları sıfırla
/combatmaster reset all

# Belirli oyuncuyu sıfırla  
/combatmaster reset <oyuncu>

# Toplu istatistik görüntüleme
/combatmaster bulk-stats
```

</details>

<details>
<summary><strong>⚡ Hızlı Komutlar</strong></summary>

**One-Click İşlemler:**
- 💥 **Tüm Combo Reset**: Sunucu geneli sıfırlama
- 🚫 **Menü Kapatma**: Tüm açık menüleri kapat
- 🔄 **Plugin Reload**: Hot-reload sistemi
- 💾 **Veri Kaydetme**: Manuel backup
- 🧹 **Cache Temizleme**: Bellek optimizasyonu
- 🚨 **Acil Durdurma**: Güvenli plugin kapatma

</details>

<details>
<summary><strong>📊 Sistem Monitörü</strong></summary>

**Performance Dashboard:**
```
🖥️ Sistem Bilgileri:
├── Java: OpenJDK 17.0.2
├── Bellek: 2.1GB / 4.0GB (52%)
├── CPU: 8 cores @ 2.4GHz
└── Uptime: 3d 14h 22m

⚡ Plugin Metrikleri:  
├── Açık Menüler: 23/50
├── Aktif Combo'lar: 8
├── Database: SQLite (OK)
└── bStats: Enabled
```

</details>

---

## 🗄️ **Database & Performans**

### 💾 **Veri Yönetimi**

<details>
<summary><strong>Database Seçenekleri</strong></summary>

**SQLite (Varsayılan):**
```yaml
database:
  type: "sqlite"
  sqlite:
    file: "combatmaster.db"
    backup-interval: 24h
```

**MySQL (Gelişmiş):**
```yaml
database:
  type: "mysql"
  mysql:
    host: "localhost"
    port: 3306
    database: "combatmaster"
    username: "user"
    password: "pass"
    pool-size: 10
```

</details>

<details>
<summary><strong>Performans Optimizasyonları</strong></summary>

**HikariCP Connection Pool:**
- ⚡ Ultra hızlı bağlantı yönetimi
- 🔄 Otomatik bağlantı yenileme  
- 📊 Real-time pool monitoring
- 🛡️ Connection leak detection

**Memory Management:**
- 🧠 Akıllı cache sistemi
- 🔥 Hot-data caching
- 🗑️ Otomatik garbage collection
- 📈 Memory usage monitoring

</details>

---

## 🌍 **Çoklu Dil Desteği**

### 🔤 **Desteklenen Diller**

<table>
<tr>
<td align="center">
🇹🇷<br>
<strong>Türkçe</strong><br>
<code>language: "tr"</code>
</td>
<td align="center">
🇬🇧<br>
<strong>English</strong><br>
<code>language: "en"</code>
</td>
</tr>
</table>

### 🔧 **Dil Özelleştirme**

**Dil Dosyası Yapısı:**
```yaml
# plugins/Combat-Master/lang/tr.yml
gui:
  titles:
    main: "&6&l⚔ Combat Master Hub ⚔"
    stats: "&e⚡ {0} İstatistikleri"
    
  items:
    best_combo: "&6🏆 En İyi Combo"
    total_hits: "&b💎 Toplam Hit"
    
combo_descriptions:
  legendary: "Efsanevi seviye! İnanılmaz."
  godlike: "Tanrısal güç! Sınırları aştınız!"
```

**Fallback Sistemi:**
```
İngilizce mesaj bulunamadı → Türkçe'ye fallback → Varsayılan mesaj
```

---

## 🎯 **Komutlar ve İzinler**

### 💬 **Oyuncu Komutları**

| Komut | Açıklama | İzin |
|-------|----------|------|
| `/combatmaster` | Ana menüyü açar | `combatmaster.use` |
| `/combatmaster gui` | Ana menüyü açar | `combatmaster.use` |
| `/combatmaster stats` | İstatistik menüsü | `combatmaster.use` |
| `/combatmaster top` | Liderlik tablosu | `combatmaster.use` |
| `/cm` | Kısa komut | `combatmaster.use` |

### ⚙️ **Admin Komutları**

| Komut | Açıklama | İzin |
|-------|----------|------|
| `/combatmaster reload` | Plugin'i yeniden yükle | `combatmaster.admin` |
| `/combatmaster reset <oyuncu>` | Oyuncu combo'sunu sıfırla | `combatmaster.admin` |
| `/combatmaster reset all` | Tüm combo'ları sıfırla | `combatmaster.admin` |
| `/combatmaster info` | Plugin bilgilerini göster | `combatmaster.admin` |

### 🔐 **İzin Sistemi**

```yaml
permissions:
  combatmaster.use:
    description: "Temel Combat Master kullanımı"
    default: true
    
  combatmaster.admin:
    description: "Admin panel erişimi"
    default: op
    children:
      - combatmaster.use
      - combatmaster.reload
      - combatmaster.reset
```

---

## 🔧 **Konfigürasyon**

### ⚙️ **Ana Ayarlar**

<details>
<summary><strong>config.yml - Tam Konfigürasyon</strong></summary>

```yaml
# ============================================
#           COMBAT MASTER v2.0.0
#     Gelişmiş Combat Tracking Plugin'i
# ============================================

# Dil Ayarları
language: "tr"  # Desteklenen: tr, en

# GUI Sistemi
gui:
  # Ses Efektleri
  sounds:
    enabled: true
    open-sound: "BLOCK_CHEST_OPEN"
    click-sound: "UI_BUTTON_CLICK"
    success-sound: "ENTITY_EXPERIENCE_ORB_PICKUP"
    error-sound: "ENTITY_VILLAGER_NO"
    
  # Performans Ayarları
  performance:
    auto-refresh-interval: 30  # Saniye
    max-open-menus: 50
    cache-duration: 300        # Saniye
    
  # Görünüm
  appearance:
    use-borders: true
    items-per-page: 21
    animated-items: true

# Combat Sistemi
combat:
  # Combo Ayarları
  combo:
    timeout-seconds: 10
    reset-on-damage: true
    
    # Renk Sistemi
    color-system:
      enabled: true
      colors:
        1-5: "&7"      # Gri
        6-10: "&f"     # Beyaz
        11-20: "&e"    # Sarı
        21-35: "&6"    # Turuncu
        36-50: "&c"    # Kırmızı
        51-75: "&d"    # Pembe
        76-100: "&5"   # Mor
        101+: "&b"     # Açık Mavi
    
    # Mesaj Sistemi
    messages:
      enabled: true
      interval: 5  # Her 5 combo'da bir mesaj
      
  # Action Bar
  actionbar:
    enabled: true
    format: "⚔️ Combo: {combo} | 🎯 Hit: {hits} | 🏅 Sıra: #{rank}"
    
  # Ses Efektleri
  sounds:
    enabled: true
    combo-sounds:
      low:    {sound: "ENTITY_PLAYER_ATTACK_STRONG", volume: 0.8, pitch: 1.0}
      medium: {sound: "ENTITY_EXPERIENCE_ORB_PICKUP", volume: 1.0, pitch: 1.2}
      high:   {sound: "ENTITY_PLAYER_LEVELUP", volume: 1.2, pitch: 1.5}
      epic:   {sound: "ENTITY_ENDER_DRAGON_GROWL", volume: 1.5, pitch: 2.0}
      
    reset-sound: {sound: "ENTITY_ITEM_BREAK", volume: 0.6, pitch: 0.8}

# Dünya Ayarları
worlds:
  enabled-worlds: []  # Boş = tüm dünyalar, örnek: ["world", "world_nether"]
  
# Hit Ayarları  
hits:
  count-mob-hits: true
  count-ground-hits: false
  critical-only: false
  
# Database
database:
  type: "sqlite"  # sqlite veya mysql
  
  sqlite:
    file: "combatmaster.db"
    backup:
      enabled: true
      interval-hours: 24
      keep-backups: 7
      
  mysql:
    host: "localhost"
    port: 3306
    database: "combatmaster"
    username: "root"
    password: ""
    pool-size: 10
    timeout: 5000
    
# Liderlik Tablosu
leaderboard:
  size: 100
  update-interval: 60  # Saniye
  
# bStats Analytics
metrics:
  enabled: true
  plugin-id: 28408

# Debug
debug:
  enabled: false
  log-combo-events: false
  log-database-queries: false
```

</details>

---

## 📈 **PlaceholderAPI Entegrasyonu**

### 🏷️ **Mevcut Placeholders**

| Placeholder | Açıklama | Örnek |
|-------------|----------|-------|
| `%combatmaster_combo%` | Mevcut combo | `25` |
| `%combatmaster_best_combo%` | En iyi combo | `87` |
| `%combatmaster_total_hits%` | Toplam hit | `1,337` |
| `%combatmaster_rank%` | Sıralamadaki yer | `#3` |
| `%combatmaster_level%` | Combat seviyesi | `Efsanevi` |

### 📊 **Kullanım Örneği**

```yaml
# TAB plugin ile kullanım
scoreboard:
  title: "&6Combat Stats"
  lines:
    - "&eCombo: &c%combatmaster_combo%"
    - "&eBest: &6%combatmaster_best_combo%"
    - "&eRank: &a%combatmaster_rank%"
```

---

## 🚨 **Sorun Giderme**

### ❓ **Sık Sorulan Sorular**

<details>
<summary><strong>Q: Menüler açılmıyor, ne yapmalıyım?</strong></summary>

**Çözüm Adımları:**
1. İzinleri kontrol edin: `/lp user <oyuncu> permission check combatmaster.use`
2. Console'da hata mesajları var mı kontrol edin
3. Plugin'in düzgün yüklendiğini doğrulayın: `/plugins`
4. Config dosyasının syntax hatası var mı kontrol edin

</details>

<details>
<summary><strong>Q: Combo değerleri gösterilmiyor?</strong></summary>

**Olası Nedenler:**
- Database bağlantı sorunu
- Dünya ayarlarında bu dünya aktif değil
- Hit detection ayarları yanlış

**Çözüm:**
```bash
/combatmaster reload  # Plugin'i yeniden yükle
```

</details>

<details>
<summary><strong>Q: Dil değişiklikleri uygulanmıyor?</strong></summary>

**Adımlar:**
1. `config.yml`'de `language: "tr"` veya `"en"` olarak ayarlayın
2. `/combatmaster reload` komutunu çalıştırın
3. Hala çalışmıyorsa sunucuyu restart edin

</details>

<details>
<summary><strong>Q: Performans sorunları yaşıyorum?</strong></summary>

**Optimizasyon:**
```yaml
# config.yml
gui:
  performance:
    max-open-menus: 25      # Düşürün
    cache-duration: 600     # Artırın
    
leaderboard:
  update-interval: 120      # Artırın
  
database:
  mysql:
    pool-size: 5            # Düşürün
```

</details>

---

## 🔄 **Güncelleme Geçmişi**

### 🆕 **v2.0.0 - Major Update**

**🎉 Yeni Özellikler:**
- ✨ Tamamen yenilenmiş GUI sistemi
- 🎨 Modern menü tasarımları  
- ⚡ Geliştirilmiş performans
- 🔧 Advanced admin araçları
- 📊 Detaylı sistem monitörü

**🛠️ Düzeltmeler:**
- 🐛 Placeholder rendering sorunları
- 🎨 Renk kodu görüntüleme hataları
- 📱 Menü pozisyonlama sorunları
- 💾 Database bağlantı istikrarı

**⚠️ Breaking Changes:**
- Config formatı güncellendi
- Bazı komutlar değişti
- Database schema güncellemesi gerekli

---

## 👨‍💻 **Geliştirici Bilgileri**

### 📁 **Proje Yapısı**

```
Combat-Master/
├── 📂 src/main/java/com/melut/combatmaster/
│   ├── 📂 gui/                    # GUI Framework
│   │   ├── 📄 BaseGUI.java       # Base menü sınıfı
│   │   ├── 📄 MenuManager.java   # Menü yöneticisi
│   │   ├── 📄 MenuListener.java  # Click handler
│   │   ├── 📂 utils/             # GUI yardımcıları
│   │   └── 📂 menus/             # Menü implementasyonları
│   │       ├── 📄 MainMenu.java
│   │       ├── 📄 StatsMenu.java
│   │       ├── 📄 LeaderboardMenu.java
│   │       └── 📄 AdminMenu.java
│   ├── 📂 managers/              # Core yöneticiler
│   │   ├── 📄 CombatManager.java # Combat logic
│   │   ├── 📄 ConfigManager.java # Config handler
│   │   └── 📄 LangManager.java   # Dil sistemi
│   ├── 📂 database/              # Database katmanı
│   │   └── 📄 DatabaseManager.java
│   ├── 📂 listeners/             # Event listeners
│   │   └── 📄 CombatListener.java
│   ├── 📂 commands/              # Komut handlers
│   │   └── 📄 CombatMasterCommand.java
│   └── 📄 CombatMaster.java      # Ana plugin sınıfı
│
├── 📂 src/main/resources/
│   ├── 📄 config.yml            # Ana konfigürasyon
│   ├── 📄 plugin.yml           # Plugin metadata
│   └── 📂 lang/               # Dil dosyaları
│       ├── 📄 tr.yml         # Türkçe
│       └── 📄 en.yml         # İngilizce
│
└── 📄 pom.xml                  # Maven konfigürasyonu
```

### 🛠️ **Build ve Geliştirme**

```bash
# Projeyi klonla
git clone https://github.com/amhunter1/Combat-Master.git
cd Combat-Master

# Maven ile build
mvn clean compile package

# Test sunucusunda çalıştır
java -jar target/Combat-Master-v2.0.0.jar
```

**Gereksinimler:**
- Java 17+ SDK
- Maven 3.8+
- Git

---

## 📊 **İstatistikler ve Analytics**

<div align="center">

### 📈 **bStats Analytics**

[![bStats Graph](https://bstats.org/signatures/bukkit/Combat-Master.svg)](https://bstats.org/plugin/bukkit/Combat-Master/28408)

**[📊 Detaylı İstatistikleri Görüntüle](https://bstats.org/plugin/bukkit/Combat-Master/28408)**

</div>

---

## 💝 **Destek ve Katkı**

<div align="center">

### 🤝 **Projeye Destek Olun**

<table>
<tr>
<td align="center">
⭐<br>
<strong>Star</strong><br>
Repo'yu yıldızlayın
</td>
<td align="center">
🐛<br>
<strong>Issues</strong><br>
Bug raporlayın
</td>
<td align="center">
💡<br>
<strong>Feature Request</strong><br>
Özellik önerin
</td>
<td align="center">
🤝<br>
<strong>Contribute</strong><br>
Kod katkısı yapın
</td>
</tr>
</table>

### 📞 **İletişim Kanalları**

**🔗 Links**
- **Discord**: [Developer](https://discord.com/users/871721944268038175)
- **GitHub**: [Repository](https://github.com/amhunter1/Combat-Master)  
- **Download**: [Modrinth](https://modrinth.com/plugin/combat-master)

</div>

---

## 📄 **Lisans**

Bu proje **kapalı kaynak** kodludur. Tüm hakları geliştiriciye aittir. Dağıtım ve kullanım koşulları için lütfen lisans dosyasını kontrol ediniz.

---

<div align="center">

## ⚔️ **Combat Master v2.0.0**

***Gelişmiş Combat Tracking Sistemi***

**Made with ❤️ by [Melut](https://github.com/amhunter1)**

---

*En iyi combat deneyimi için teşekkürler! 🎮*

</div>
