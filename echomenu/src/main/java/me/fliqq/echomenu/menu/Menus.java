package me.fliqq.echomenu.menu;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.button.annotation.Position;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.slider.ColoredTextSlider;
import org.mineacademy.fo.slider.Slider;

import com.massivecraft.factions.tag.PlayerTag;

import me.fliqq.echomenu.EchoMenu;
import me.fliqq.echopickaxe.manager.BackPacksManager;
import me.fliqq.echopickaxe.manager.PPickaxeManager;
import me.fliqq.echopickaxe.manager.PlayerEnchant;
import me.fliqq.echopickaxe.objects.BackPacks;
import me.fliqq.echopickaxe.objects.PPickaxes;

public class Menus extends Menu {

    final PPickaxeManager pM = PPickaxeManager.getInstance();
    final BackPacksManager bM = BackPacksManager.getInstance();

    @Position(23)
    private final Button chatMenuButton;

    @Position(13)
    private final Button profileButton;

    @Position(21)
    private final Button piocheButton;

    public Menus() {
        this.setSize(9 * 5);
        this.setTitle("Menu Principal");

        // MENU CHAT
        this.chatMenuButton = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                new ChatMenu().displayTo(player);
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.PINK_WOOL, "&dPersonnalisation", "", "&7Ouvre le menu chat").glow(false).make();
            }
        };

        this.profileButton = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                // TODO Auto-generated method stub
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.PLAYER_HEAD, "&eProfil du joueur").skullOwner(getViewer().getName()).make();
            }
        };

        this.piocheButton = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                new PiocheMenu(player).displayTo(player);
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.DIAMOND_PICKAXE, "&9Menu Pioche", "", "&bAméliore ta pioche", "et ton sac à dos").glow(true).makeMenuTool();
            }
        };
    }

    @Override
    protected void onPostDisplay(Player player) {
        this.animate(20, this::redrawButtons);
        final String menuTitle = "Menu Principal";
        final Slider<String> textSlider = ColoredTextSlider
                .from(menuTitle)
                .width(7)
                .primaryColor("&a&l")
                .secondaryColor("&f&l");

        this.animateAsync(5, () -> setTitle(textSlider.next()));
    }

    public class PiocheMenu extends Menu {

        @Position(13)
        private final Button pickaxeButton;
        @Position(30)
        private final Button enchantButton;
        @Position(38)
        private final Button pickSlotButton;
        @Position(32)
        private final Button bpButton;
        @Position(42)
        private final Button bpSlotButton;

        public PiocheMenu(Player player) {
            super(Menus.this);
            this.setSize(9 * 5);
            this.setTitle("&9&lMenu Pioche");

            this.pickaxeButton = new Button() {
                @Override
                public ItemStack getItem() {
                    return ItemCreator.of(pM.getPick(player.getUniqueId()).getPickItemStack()).make();
                }

                @Override
                public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                }
            };

            this.bpButton = new Button() {
                @Override
                public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                    new BpMenu(player).displayTo(player);
                }

                @Override
                public ItemStack getItem() {
                    return ItemCreator.of(CompMaterial.CHEST, "&6&lMenu sac à dos", "", "&eAugmente la taille", "&ede ton sac à dos").make();
                }
            };

            this.enchantButton = new Button() {
                @Override
                public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                    new EnchantsMenu(player).displayTo(player);
                }

                @Override
                public ItemStack getItem() {
                    return ItemCreator.of(CompMaterial.BOOK, "&5&lMenu enchantements", "", "&aAméliore ta pioche").glow(true).make();
                }
            };

            this.bpSlotButton = new Button() {
                @Override
                public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                    new SlotMenu(player, true, player.getUniqueId()).displayTo(player);
                }

                @Override
                public ItemStack getItem() {
                    return ItemCreator.of(CompMaterial.COMMAND_BLOCK_MINECART, "&7&lSlot sac à dos", "", "&fchange l'emplacement").make();
                }
            };
            this.pickSlotButton = new Button() {
                @Override
                public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                    new SlotMenu(player, false, player.getUniqueId()).displayTo(player);
                }

                @Override
                public ItemStack getItem() {
                    return ItemCreator.of(CompMaterial.COMMAND_BLOCK_MINECART, "&7&lSlot pioche", "", "&fchange l'emplacement").make();
                }
            };
        }
    }

    private class SlotMenu extends Menu {
        
        @Position(0) private final Button slot0Button;
        @Position(1) private final Button slot1Button;
        @Position(2) private final Button slot2Button;
        @Position(3) private final Button slot3Button;
        @Position(4) private final Button slot4Button;
        @Position(5) private final Button slot5Button;
        @Position(6) private final Button slot6Button;
        @Position(7) private final Button slot7Button;
        @Position(8) private final Button slot8Button;

        private SlotMenu(Player player, Boolean bool, UUID uuid) {
            super(Menus.this);
            this.setSize(9 * 2);
            this.setTitle("&7&lFixe l'emplacement");

            this.slot0Button = createSlotButton(0, bool, uuid);
            this.slot1Button = createSlotButton(1, bool, uuid);
            this.slot2Button = createSlotButton(2, bool, uuid);
            this.slot3Button = createSlotButton(3, bool, uuid);
            this.slot4Button = createSlotButton(4, bool, uuid);
            this.slot5Button = createSlotButton(5, bool, uuid);
            this.slot6Button = createSlotButton(6, bool, uuid);
            this.slot7Button = createSlotButton(7, bool, uuid);
            this.slot8Button = createSlotButton(8, bool, uuid);
        }

        private Button createSlotButton(int index, Boolean bool, UUID uuid) {
            PPickaxes pickaxe = pM.getPick(uuid);

            BackPacks backpack = bM.getBackPack(uuid);

            if (pickaxe.getPickSlot() == index) {
                return new Button() {
                    @Override
                    public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                    }

                    @Override
                    public ItemStack getItem() {
                        return ItemCreator.of(CompMaterial.CHEST_MINECART, "&a&lSLOT PIOCHE").glow(true).make();
                    }
                };
            }

            if (backpack.getBpSlot() == index) {
                return new Button() {
                    @Override
                    public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                    }

                    @Override
                    public ItemStack getItem() {
                        return ItemCreator.of(CompMaterial.CHEST_MINECART, "&a&lSLOT SAC").glow(true).make();
                    }
                };
            }
            else {
	
	            return new Button() {
	                @Override
	                public void onClickedInMenu(Player player, Menu menu, ClickType click) {
	                    if (bool) {
	                        backpack.setBpSlot(index);
	                    } else {
	                        pickaxe.setPickSlot(index);
	                    }
	                }
	
	                @Override
	                public ItemStack getItem() {
	                    return ItemCreator.of(CompMaterial.MINECART, "Slot: &8" + index).glow(false).make();
	                }
	            };
            }
        }
        //FONCTIONNE PAS !!!!!!!
        @Override
        protected void onPostDisplay(Player player) {
            this.animate(20, this::redrawButtons);
        }
    }
    

    
	private class EnchantsMenu extends Menu {
		
		@Position(13)
		private final Button pickaxeButton;
		
		@Position(27)
		private final Button effButton;
		
		@Position(28)
		private final Button fortButton;
		
		@Position(29)
		private final Button expButton;
		
		private EnchantsMenu(Player player) {
			super(Menus.this);
			this.setSize(9*5);
			this.setTitle("&9&lEnchantements");
			
			PPickaxes pPickaxe = pM.getPick(player.getUniqueId());
			this.pickaxeButton = new Button() {

				@Override
				public void onClickedInMenu(Player player, Menu menu, ClickType click) {
					
				}

				@Override
				public ItemStack getItem() {
					return ItemCreator.of(pM.getPick(player.getUniqueId()).getPickItemStack()).make();
				}
				
			};
			this.effButton = new Button() {
				
				@Override
				public void onClickedInMenu(Player player, Menu menu, ClickType click) {
					
					pPickaxe.payEnchIfEnoughToken(PlayerEnchant.getEnchant("Efficiency"), 1);
				}

				@Override
				public ItemStack getItem() {
					return ItemCreator.of(CompMaterial.BOOK, "&b&lEfficacité", "", "Click-gauche pour ajouter 1 niveau").glow(true).make();
				}
				
			};
			this.fortButton = new Button() {

				@Override
				public void onClickedInMenu(Player player, Menu menu, ClickType click) {
					pPickaxe.payEnchIfEnoughToken(PlayerEnchant.getEnchant("Fortune"), 1);
				}

				@Override
				public ItemStack getItem() {
					return ItemCreator.of(CompMaterial.BOOK, "&b&lFortune", "", "Click-gauche pour ajouter 1 niveau").glow(true).make();
				}
			};
			this.expButton = new Button() {

				@Override
				public void onClickedInMenu(Player player, Menu menu, ClickType click) {
					pPickaxe.payEnchIfEnoughToken(PlayerEnchant.getEnchant("Explosion"), 1);
				}

				@Override
				public ItemStack getItem() {
					return ItemCreator.of(CompMaterial.BOOK, "&b&lExplosion", "", "Click-gauche pour ajouter 1 niveau").glow(true).make();
				}
				
			};
		}
	}
	private class BpMenu extends Menu {
		
		@Position(2)
		private final Button bpButton;
		
		@Position(4)
		private final Button add1Button;
		
		@Position(6)
		private final Button add10Button;
		private BpMenu(Player player) {
			super(Menus.this);
			this.setSize(9*1);
			this.setTitle("&6&lSac à dos");
			this.bpButton = new Button() {

				@Override
				public void onClickedInMenu(Player player, Menu menu, ClickType click) {
					
				}

				@Override
				public ItemStack getItem() {
					return ItemCreator.of(bM.getBackPack(player.getUniqueId()).getBpItemStack()).make();
				}
				
			};
			this.add1Button = new Button() {

				@Override
				public void onClickedInMenu(Player player, Menu menu, ClickType click) {
					bM.getBackPack(player.getUniqueId()).addLvlIfGems(1);
				}

				@Override
				public ItemStack getItem() {
					return ItemCreator.of(CompMaterial.ENDER_CHEST, "&a&lAméliore de 1 niveau","&7Prix: &e&l500 gemmes").glow(true).make();
				}
				
			};
			
			this.add10Button = new Button() {

				@Override
				public void onClickedInMenu(Player player, Menu menu, ClickType click) {
					bM.getBackPack(player.getUniqueId()).addLvlIfGems(10);

				}

				@Override
				public ItemStack getItem() {
					return ItemCreator.of(CompMaterial.ENDER_CHEST, "&a&lAméliore de 10 niveaux","&7Prix: &e&l5000 gemmes").glow(true).make();
				}
				
			};
			
		}
	}
	
	private class ChatMenu extends Menu {
		
	
		@Position(13)
		private final Button chatColorButton;
		@Position(30)
		private final Button toggleGlowButton;

		private ChatMenu() {
			super(Menus.this);
			this.setSize(9*5);
			//this.setTitle("&4M&6e&en&au &cC&bo&dl&9o&3u&5r &cC&6h&ea&at");
			this.setTitle("&d&lMenu personnalisation");
			this.chatColorButton = new Button() {

				@Override
				public void onClickedInMenu(Player player, Menu menu, ClickType click) {
					new ChatColorMenu().displayTo(player);;
					
				}

				@Override
				public ItemStack getItem() {
					return ItemCreator.of(CompMaterial.PINK_WOOL, "&cC&bo&dl&9o&3u&5r &cC&6h&ea&at", "", "&7Change ta couleur de chat").glow(true).make();
				}
				
			};
			this.toggleGlowButton = new Button() {

				@Override
				public void onClickedInMenu(Player player, Menu menu, ClickType click) {
					if(player.isGlowing())
						player.setGlowing(false);
					else
						player.setGlowing(true);
				}		

				@Override
				public ItemStack getItem() {
					return ItemCreator.of(CompMaterial.GLOWSTONE_DUST, "&a&lGlow", "", "&7Active ou désactive le glow").glow(true).make();
				}
				
			};
			

		}
	}
	
	
    private class ChatColorMenu extends Menu {
        private final String[] colorNames = {
                "&l&fBlanc", "&l&6Orange", "&l&dMagenta", "&l&bBleu Clair",
                "&l&eJaune", "&l&aCitron Vert", "&l&dRose", "&l&8Gris",
                "&l&7Gris Clair", "&l&3Cyan", "&l&5Violet", "&l&9Bleu",
                "&l&6Marron", "&l&2Vert", "&l&cRouge", "&l&0Noir"
        };

        private final CompMaterial[] woolMaterials = {
                CompMaterial.WHITE_WOOL, CompMaterial.ORANGE_WOOL, CompMaterial.MAGENTA_WOOL, CompMaterial.LIGHT_BLUE_WOOL,
                CompMaterial.YELLOW_WOOL, CompMaterial.LIME_WOOL, CompMaterial.PINK_WOOL, CompMaterial.GRAY_WOOL,
                CompMaterial.LIGHT_GRAY_WOOL, CompMaterial.CYAN_WOOL, CompMaterial.PURPLE_WOOL, CompMaterial.BLUE_WOOL,
                CompMaterial.BROWN_WOOL, CompMaterial.GREEN_WOOL, CompMaterial.RED_WOOL, CompMaterial.BLACK_WOOL
        };

        // Buttons for each color
        @Position(0) private final Button whiteButton = createColorButton(0);
        @Position(1) private final Button orangeButton = createColorButton(1);
        @Position(2) private final Button magentaButton = createColorButton(2);
        @Position(3) private final Button lightBlueButton = createColorButton(3);
        @Position(4) private final Button yellowButton = createColorButton(4);
        @Position(5) private final Button limeButton = createColorButton(5);
        @Position(6) private final Button pinkButton = createColorButton(6);
        @Position(7) private final Button grayButton = createColorButton(7);
        @Position(8) private final Button lightGrayButton = createColorButton(8);
        @Position(9) private final Button cyanButton = createColorButton(9);
        @Position(10) private final Button purpleButton = createColorButton(10);
        @Position(11) private final Button blueButton = createColorButton(11);
        @Position(12) private final Button brownButton = createColorButton(12);
        @Position(13) private final Button greenButton = createColorButton(13);
        @Position(14) private final Button redButton = createColorButton(14);
        @Position(15) private final Button blackButton = createColorButton(15);

        private ChatColorMenu() {
            super(Menus.this);
            this.setSize(9 * 2);
            this.setTitle("&bChatColor menu");
        }

        private Button createColorButton(int index) {
            return new Button() {
                @Override
                public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                    // Handle color change logic
                }

                @Override
                public ItemStack getItem() {
                    return ItemCreator.of(woolMaterials[index], colorNames[index]).glow(false).make();
                }
            };
        }
    }
}
