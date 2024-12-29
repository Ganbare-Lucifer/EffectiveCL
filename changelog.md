------------------------------------------------------
Effective 2.4 (Alpha) - 1.21.1
------------------------------------------------------
As Effective is getting updated to 1.21 and beyond, the mod will move away from Lodestone to instead use Veil.
As a result it requires a big rewrite, and instead of doing one big update, I figured I would do multiple alpha releases as I gradually work on it.

- Replaced the Lodestone library with the Veil library
- All the visual effects using Lodestone have been temporarily disabled until they get rewritten to use Veil. Temporarily removed:
  - Chorus petals
  - Will o' Wisps
  - Allay trails
  - Improved flames
  - Improved dragon breath
  - Underwater chest bubbles
  - Waterfall clouds and mist
  - Fireflies
  - All screenshake effects
- Rewrote splashes and glow splashes
- Fixed inconsistencies with droplets sometimes appearing too far from the splash

------------------------------------------------------
Effective 2.3.2 - 1.20.1
------------------------------------------------------
- Adjusted firefly spawning to be within 5 blocks off to the ground (instead of mostly in the sky like before)
- Added a soft wind ambience to swamp biomes (eg. mangroves being hot made them have no background wind)
- Added a unique mangrove bird ambience during the day
- Changed the cave ambience detection to use solid stone-like blocks above the player instead of light level
    - This fixes some issues that would happen for instance in swamps, where being under a tree and lower than the sea
      level would start playing the cave ambience
- Added a new reverbed foliage rustling sound for lush caves instead of the regular forest one
- Added a check for dripstone cave biomes up to 20 blocks above the player for its ambience
    - This should fix the unreliability of the biome's layout often not reaching the ground where there are no
      stalagmites
- Fixed the ULTRAKILL parry effect missing the white flash
    - The parry effect now uses a direct overlay render instead of Lodestone screen particles
- Reduced the ripple quantity when it rains
- Increased the rain ripple density default config value
- Increased the cascade sound distance default config value

------------------------------------------------------
Effective 2.3.1 - 1.20.1
------------------------------------------------------
- Reworked all audio ambience to make it more dynamic. Different ambient effects will now stack on top of each other
  depending on the environment's properties.
    - Added bees in floral biomes during the day
    - Added birds in forests during the day
    - Added cicadas in savannas during day
    - Added crickets in temperate (excluding swamps to use their dedicated cricket and frog ambience instead) and floral
      biomes at night
    - Added frogs and crickets in swamps at night
    - Added day jungle animals in jungles during the day
    - Added night jungle animals in jungles at night
    - Added owls in forests at night
    - Added rustling foliage in forests, floral biomes, swamps, jungles, wooded badlands and lush caves
    - Added water dripping in dripstone caves
    - Added water streams in lush caves
    - Added water flowing in rivers
    - Added water waves in beaches and oceans
    - Added arid wind in deserts and mesas
    - Added cave wind in caves (excluding the deep dark to use its dedicated ambience instead)
    - Added cold wind in cold biomes (excluding peaks to use their dedicated wind instead) and mountain slopes
    - Added deep dark ambience (classified as wind)
    - Added end ambience (classified as wind)
    - Added mountain wind in peaks
    - Added soft wind in temperate, floral, savanna, jungle and mushroom field biomes
- Split ambient effects into 4 categories (animals, foliage, water and wind) and added volume configuration for each
    - Allows the user to fine tune their audio ambience exactly how they want. If you don't like animal sounds in
      ambience mods for instance, you can completely disable them and keep the rest!
- Changed cascade audio detection to make it so cascades underground can only be heard by players underground, and
  surface cascades can only be heard by players on the surface, to reduce the amount of cascade audio coming from
  underground sources.

------------------------------------------------------
Effective 2.3 - 1.20.1
------------------------------------------------------
- Added audio ambience!
    - Note: This feature is still early in development and some biomes might be missing. Please don't hesitate to give
      feedback on missing ambience and other requests on
      the [GitHub issues](https://github.com/Ladysnake/Effective/issues)!
    - Added day ambience for temperate biomes
    - Added night ambience for temperate biomes
    - Added beach ambience (same for day and night)
    - Added cave ambience
    - Added deep dark ambience
    - Added lush caves ambience
    - Added dripstone caves ambience
- Rearranged the config to now be separated between "Visuals" and "Audio" categories
    - Separated the visuals category in sub-categories (corresponding to the previous categories)
    - Moved the cascade sound volume and distance options to the audio category
- Added a volume option for biome ambience

------------------------------------------------------
Effective 2.2.2.1 - 1.20.1
------------------------------------------------------
- Made dark sculk dust properly respect environment lighting

------------------------------------------------------
Effective 2.2.2 - 1.20.1
------------------------------------------------------
- Ported to Fabric
- Changed fireflies to only spawn above sea level
- Slightly increased ripple particle size
- Reduced glow squid hypnosis maximum distance to 7 blocks
- Changed the scaling up of improved glow squid particles to be slower
- Decreased the strength of waterfall mist
- Increased the quantity of waterfall cloud particles in the default config
- Decreased the distance to hear cascades in the default config
- Changed waterfall clouds and mist to fade with depth (thanks Aidan! <3)

------------------------------------------------------
Effective 2.2.1 - 1.20.1
------------------------------------------------------
- Added more config options for the splash effect, screen shake, and trails (thanks donkeyrat !)
- Added a Dutch localization (thanks cph101 !)
- Fixed a bug causing the accessibility onboard screen and the main menu fade-in to be skipped (thanks Ennui !)
- Fixed splash being centered on the nearest block rather than the entity that jumped into the water

------------------------------------------------------
Effective 2.2.0 - 1.20.1
------------------------------------------------------
Brought to you on 1.20.1 by the combined efforts of SzczurekYT and Motschen (big thanks to them !)

- Updated to 1.20.1
- Updated config screen
- Added Swedish localization (Thanks Dontknow09 !)
- Fixed crash when Satin was not installed separately
- *Dev thing: changed maven group and package to `org.ladysnake`*

------------------------------------------------------
Effective 2.1.2 - 1.19.2
------------------------------------------------------
- Updated Ukrainian localization thanks to unroman

------------------------------------------------------
Effective 2.1.1 - 1.19.2
------------------------------------------------------
- Added two new choices to the Allay trail option to either have trail only, twinkle only, both or none
- Fixed rain ripples not being displayed at the correct position
- Fixed broken chorus flowers spawning too few chorus petals
- Fixed ULTRAKILL parry effect being too bright
- Fixed the NEVER Eyes in the Dark config options not having a lang entry

------------------------------------------------------
Effective 2.1 - 1.19.2
------------------------------------------------------
- Added the ability to make cascades silent
    - Placing wool under the receiving water source of the cascade now makes cascades silent
- Added the ability to color cascade mist
    - Placing coloured wool under the receiving water source of the cascade will now color the mist according to the
      wool block's color
- Fixed cascades with water flowing from straight up creating mist starting at 3 adjacent source blocks instead of 4
- Fixed lingering potion effects using the improved dragon breath particles
- Limited cascade sound volume multiplier to 100% and set the default to 30%
- Fixed flowing water clouds causing mist

------------------------------------------------------
Effective 2.0 - 1.19.2
------------------------------------------------------
- Rewrote the mod from scratch
- Merged some Illuminations features into Effective. Here's a list of all the Illuminations features that made it in:
    - Added and improved Fireflies
        - Fireflies now disappear faster and spawn more frequently, giving the effect a more consistent look
        - Fixed fireflies not being attracted to light
        - Fixed various bugs like fireflies stopping or teleporting
    - Added and improved Chorus Petals:
        - Chorus petals now stay around approximately 10x longer and fade into their blueish purple state more slowly
        - Chorus petals now lay flat when touching the ground or water
        - Chorus petals now cause ripples when landing on water
    - Added Eyes in the Dark (on Halloween)
    - Added and completely overhauled Will o' Wisps
    - Added Illuminations cosmetics (though they may get removed or reworked in the future)
        - Reworked the Will o' Wisp pet cosmetic to allow players to choose two custom colors to personalize their Will
          o' Wisp
        - As a result, Golden Will and Founding Skull cosmetics were removed as they can be replicated with custom
          colors
- Improved fireball visuals
- Improved dragon fireball and dragon breath visuals
- Added sculk dust particles
- Added spectral arrows trails and made them emissive
- Added air bubbles coming out of chests when opened underwater
- Improved cascades
    - Waterfall cloud particles now have an animated texture
    - Waterfall clouds will now only generate if water is flowing into a 2 block deep water source with at least one
      adjacent source block
    - Waterfall cloud size and cascade sound pitch are now dependent on the strength of the flowing water as well as the
      amount of water sources it is flowing into
    - Waterfalls with the strongest current (flowing directly from above) flowing into 4 water sources now generate a
      mist effect
    - Waterfall cloud density as well as mist density can be customized in the configuration options
- Improved Glow Squid particles
- Rewrote the configuration from scratch and added options to control whether the effect is enabled and / or its density
  for all effects
- Added sliders in the Mod Menu config for all numerical configuration options
- Merged both configuration options of Glow Squid hypnotizing into one
- Added a new secret config option for a new secret effect :)

------------------------------------------------------
Effective 1.6 - 1.19.2
------------------------------------------------------
- Added a screen shake effect for:
    - Wardens roaring
    - Wardens using their sonic boom attack
    - Ravagers roaring after being stunned
    - The Ender Dragon's long roar when perched
- Added four new config options to disable each screen shake trigger individually
- Removed the need for waterfalls to be visible by the player in order to play sound
- Added Ukrainian translation, thanks to ttrafford7 and V972!
- Added Portuguese (Brazil) translation, thanks to FITFC!

------------------------------------------------------
Effective 1.5 - 1.19.2
------------------------------------------------------
- Added Allay trails
    - Allay will now leave a colored trail and twinkles when flying around, inspired from the Minecraft Legends announce
      trailer
    - Allay trails can be disabled and trail twinkle density can be configured
- Added golden Allays
    - Allays now have a 50% chance of being a golden variant, also inspired from the Minecraft Legends announce trailer
    - Can be disabled in the configuration

------------------------------------------------------
Effective 1.4.1 - 1.19.2
------------------------------------------------------
- Added glowing plankton waterfall clouds in warm oceans at night
- Fixed splash rims looking incorrectly with Sodium
- Waterfalls no longer play sound if the player cannot see them
- Now includes Satin API
- Migrated the mod to Quilt

------------------------------------------------------
Effective 1.4 - 1.19.2
------------------------------------------------------
- Hypnotizing glow squids
    - Glow squids can now hypnotize you, displaying a hypnotizing shader that gradually gets stronger the longer you
      look at them
    - Glow squids will attract your cursor if they appear on your screen, this attraction getting stronger with the
      shader
    - Both cursor attraction and hypnotizing can be disabled with two new configuration options
- Glow squids named "jeb_" now glow rainbow and have a rainbow hypnotizing pattern
- Splashes and ripples (from rain or splashes) in warm oceans at night will now glow from glowing plankton
    - Glowing plankton can be disabled with a new configuration option

------------------------------------------------------
Effective 1.3 - 1.19.2
------------------------------------------------------
- Overhauled splashes:
    - Splashes will now be colored depending on the water they originate from
    - Reworked the splash texture to be more fitting with the vanilla Minecraft aesthetic
    - Added a config option to adjust the transparency of the splashes' white rim
- Added ripples on water when it's raining
    - Ripple density can be adjusted or disabled in the config
- Added a config option to control the density or remove flowing water splashing particles
- Added two new config options to adjust cascade sounds volume and distance
- Fixed splashes sometimes being completely black (thanks to lonefelidae16)
- Fixed occasional crashes that could happen randomly (thanks to lonefelidae16)
- Widened the logic to fix splashes sometimes not appearing as well as now taking into account waterlogged blocks (like
  kelp or seagrass) and not just water source blocks
- Fishing bobbers no longer produce splashes or droplet particles
- Changed config library from Cloth Config to MidnightLib
- Updated to Minecraft 1.19.2

------------------------------------------------------
Effective 1.2.2 - 1.19
------------------------------------------------------
- Updated to Minecraft 1.19

------------------------------------------------------
Effective 1.2.1 - 1.18.2
------------------------------------------------------
- Updated to Minecraft 1.18.2

------------------------------------------------------
Effective 1.2 - 1.18.1
------------------------------------------------------
Massive thanks to MoriyaShiine for most of these additions and changes!

- Lapis lazuli block updates (piston moved, placed, broken...) connected to a flowing water block now cause connected
  flowing water blocks to spawn waterfall particles
- Updating flowing water blocks now spawns waterfall particles
- Completely remade waterfall sounds to be more distinct from rain sound effects and improve background ambience in
  areas with cascades
- Added different configuration options and mod menu compatibility to easily access these options
- Increased cascade particle generation distance
- Massively improved performance and waterfall calculations
- Massively improved performance with dynamic audio mods such as Sound Physics Remastered
- Fixed concurrent modification crashes

------------------------------------------------------
Effective 1.1.1 - 1.18.1
------------------------------------------------------
- Fixed the first wave of splash particles being inverted
- Made waterfall sound effects quieter yet still present far away from them
- Removed lava splashes for now, as they were causing more issues than adding to the ambience
- Removed cascades appearing underwater when no air is present

------------------------------------------------------
Effective 1.1 - 1.18.1
------------------------------------------------------
- Added two settings to enable or disable splashes and cascade effects available in the config file of your Minecraft
  instance config folder, thanks to devpelux
- Implemented various optimizations and performance fixes, thanks to Sollace
- Fixed the cascade sounds playing too many times resulting in them being louder than they are supposed to be, thanks to
  Sollace
- Fixed Canvas and Sodium cascade incompatibilities, thanks to spiralhalo
- Added russian subtitles, thanks to Felix14-v2

------------------------------------------------------
Effective 1.0 - 1.18
------------------------------------------------------
Initial release with water splashes, lava splashes and cascade effects.
