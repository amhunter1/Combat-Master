# Combat-Master
# Combat Master

Gelişmiş combo sayacı, ses efektleri, miss (ıskalama) algılama, renk sistemi, veritabanı ve liderlik tablosu özelliklerine sahip bir Minecraft (Spigot/Paper) eklentisi.

A powerful Minecraft (Spigot/Paper) combat plugin with advanced combo counter, sounds, miss detection, color system, database and leaderboard.

---

## 🇹🇷 Türkçe

### Özellikler
- Gelişmiş ses sistemi: combo seviyesine göre farklı sesler, miss ve reset sesleri (`src/main/resources/config.yml`/`sounds`).
- Zengin combo mesajları ve action bar formatı (`combo.messages`, `combo.actionbar`).
- Miss (ıskalama) algılama ve opsiyonel combo sıfırlama (`combo.miss-system`).
- Combo sayısına göre renk sistemi (`combo.color-system`).
- SQLite (varsayılan) ve MySQL desteği, HikariCP bağlantı havuzu (`database`).
- Liderlik tablosu ve periyodik güncelleme (`leaderboard`).
- PlaceholderAPI entegrasyonu (opsiyonel, `softdepend`).
- Komutlar ve izinler ile yönetim.

### Gereksinimler
- Java 17+
- Spigot/Paper 1.20+
- (Opsiyonel) PlaceholderAPI

### Kurulum
1. Yayınlanan jar dosyasını `plugins/` klasörüne kopyalayın.
2. Sunucuyu başlatın ve `plugins/Combat-Master/` altında `config.yml` oluşturulmasına izin verin.
3. Gerekirse `config.yml` dosyasını düzenleyin.
4. (Opsiyonel) PlaceholderAPI kurun ve gerekli placeholderları kullanın.

### Konfigürasyon
`src/main/resources/config.yml` ana anahtarları:
- `sounds.enabled`: Sesleri aç/kapat.
- `sounds.combo-sounds.low|medium|high|epic`: `sound`, `volume`, `pitch`.
- `sounds.miss-sound`: `enabled`, `sound`, `volume`, `pitch`.
- `sounds.reset-sound`: `enabled`, `sound`, `volume`, `pitch`.
- `combo.messages`: Action bar mesaj döngüsü.
- `combo.message-interval`: Kaç vuruşta bir mesaj.
- `combo.actionbar.enabled`, `combo.actionbar.format`: Örn. `&e{combo}x &a{message}`.
- `combo.color-system.enabled`, `combo.color-system.colors`: Aralık -> renk `&` kodu.
- `combo.timeout-seconds`: Son vuruştan sonra combo sıfırlama süresi.
- `combo.miss-system.enabled`, `combo.miss-system.reset-on-miss`: Miss algılama ve sıfırlama.
- `enabled-worlds`: Boş değilse sadece bu dünyalarda aktif.
- `database.type`: `sqlite` veya `mysql`.
  - `database.sqlite.file`: SQLite dosya adı.
  - `database.mysql.*`: `host`, `port`, `database`, `username`, `password`, `ssl`.
- `leaderboard.size`, `leaderboard.title`, `leaderboard.format`, `leaderboard.update-interval`.
- `messages.*`: İzin, reload, oyuncu bulunamadı, dünya pasif mesajları.

Renk ve formatlarda `&` renk kodları desteklenir. Yer tutucular:
- `{combo}`: Mevcut combo sayısı
- `{message}`: Mesaj döngüsündeki metin
- `leaderboard.format` içinde: `{rank}`, `{player}`, `{combo}`

### Komutlar
- `/combatmaster [reload|stats|reset|info]`
  - `reload`: Konfigürasyonu yeniden yükler.
  - `stats [player]`: Oyuncu istatistiklerini gösterir.
  - `reset [player]`: Oyuncunun verilerini sıfırlar.
  - `info`: Eklenti hakkında bilgi.
- `/leaderboard` (alias: `/lb`, `/top`): Liderlik tablosunu gösterir.

### İzinler
- `combatmaster.admin` (varsayılan: `op`): Yönetim komutlarına erişim.
- `combatmaster.use` (varsayılan: `true`): Temel kullanım ve `/leaderboard` erişimi.

### Placeholderlar (PlaceholderAPI)
Aşağıdaki örnekler PAPI ile kullanılabilir (PAPI yüklüyse):
- `%combatmaster_combo%`: Anlık combo.
- `%combatmaster_best_combo%`: Oyuncunun en iyi combosu.
- `%combatmaster_total_hits%`: Toplam vuruş.
- `%combatmaster_rank%`: Sıralamadaki yeri (yaklaşık/önbellekli olabilir).

Not: Bazı placeholderlar performans için asenkron/önbellekli dönebilir.

### Veritabanı
- Varsayılan: SQLite (`plugins/Combat-Master/combatmaster.db`).
- MySQL için `database.type: mysql` ve bağlantı bilgilerini doldurun.

### SSS
- Combo neden sıfırlanıyor? `combo.timeout-seconds` süresi dolduğunda veya miss (ıskalama) olduğunda (`reset-on-miss: true`).
- Sesler çalışmıyor? `sounds.enabled: true` olduğundan ve sound adlarının sürümünüzle uyumlu olduğundan emin olun.
- Bazı dünyalarda çalışmıyor? `enabled-worlds` listesini kontrol edin.

---

## 🇬🇧 English

### Features
- Advanced sound system by combo tiers, miss and reset sounds (`config.yml`/`sounds`).
- Rich combo messages and action bar formatting (`combo.messages`, `combo.actionbar`).
- Miss detection with optional combo reset (`combo.miss-system`).
- Combo-based color system (`combo.color-system`).
- SQLite (default) and MySQL support via HikariCP (`database`).
- Leaderboard with periodic refresh (`leaderboard`).
- Optional PlaceholderAPI integration (`softdepend`).
- Commands and permissions for administration.

### Requirements
- Java 17+
- Spigot/Paper 1.20+
- (Optional) PlaceholderAPI

### Installation
1. Drop the released jar into the `plugins/` folder.
2. Start the server to generate `config.yml` under `plugins/Combat-Master/`.
3. Edit `config.yml` as needed.
4. (Optional) Install PlaceholderAPI to use placeholders.

### Configuration
Main keys in `src/main/resources/config.yml`:
- `sounds.enabled`
- `sounds.combo-sounds.low|medium|high|epic` with `sound`, `volume`, `pitch`
- `sounds.miss-sound` and `sounds.reset-sound`
- `combo.messages`, `combo.message-interval`
- `combo.actionbar.enabled`, `combo.actionbar.format`
- `combo.color-system.enabled`, `combo.color-system.colors`
- `combo.timeout-seconds`
- `combo.miss-system.enabled`, `combo.miss-system.reset-on-miss`
- `enabled-worlds`
- `database.type`, `database.sqlite.file`, `database.mysql.*`
- `leaderboard.size`, `leaderboard.title`, `leaderboard.format`, `leaderboard.update-interval`
- `messages.*`

Placeholders in formats:
- `{combo}`, `{message}` for action bar
- `{rank}`, `{player}`, `{combo}` for leaderboard format

### Commands
- `/combatmaster [reload|stats|reset|info]`
  - `reload`: Reloads configuration.
  - `stats [player]`: Shows player stats.
  - `reset [player]`: Resets player data.
  - `info`: About plugin.
- `/leaderboard` (aliases: `/lb`, `/top`): Shows leaderboard.

### Permissions
- `combatmaster.admin` (default: `op`): Access to admin commands.
- `combatmaster.use` (default: `true`): Basic usage and `/leaderboard`.

### Placeholders (PlaceholderAPI)
- `%combatmaster_combo%`
- `%combatmaster_best_combo%`
- `%combatmaster_total_hits%`
- `%combatmaster_rank%`

Note: Some placeholders may be async/cached for performance.

### Database
- Default SQLite at `plugins/Combat-Master/combatmaster.db`.
- For MySQL set `database.type: mysql` and configure credentials.

### FAQ
- Why is my combo resetting? Timeout (`combo.timeout-seconds`) or miss when `reset-on-miss: true`.
- No sounds? Ensure `sounds.enabled: true` and sound names match your server version.
- Not active in some worlds? Check `enabled-worlds` list.

---

## Lisans / License
Bu proje kapalı kaynak bir örnektir; dağıtım koşulları proje sahibine aittir. Aksi belirtilmedikçe tüm hakları saklıdır.

This project is a closed-source example; distribution terms belong to the owner. All rights reserved unless stated otherwise.

