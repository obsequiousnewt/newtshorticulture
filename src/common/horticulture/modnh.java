package horticulture;

import horticulture.fruit.GeneratorFruitTrees;
import horticulture.fruit.ItemExplodingLemon;
import horticulture.fruit.ItemFruit;
import horticulture.fruit.flower.BlockFruitFlower;
import horticulture.fruit.trees.BlockFruitHanging;
import horticulture.fruit.trees.BlockFruitLeaves;
import horticulture.fruit.trees.BlockFruitLog;
import horticulture.fruit.trees.BlockFruitSapling;
import horticulture.fruit.trees.ItemBlockFruitLeaves;
import horticulture.fruit.trees.ItemBlockFruitSapling;
import horticulture.mill.BlockMillstones;
import horticulture.tractors.EntityTractor;
import horticulture.tractors.ItemTractor;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import net.minecraft.src.Block;
import net.minecraft.src.Chunk;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.StringTranslate;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 */
@Mod(modid=modnh.modid,name=modnh.shortName,version=modnh.version)
@NetworkMod(clientSideRequired=true,serverSideRequired=false)
public final class modnh{
	//First, a few constants that have little/no effect on the code.
	public static final String modid = "horticulture";
	public static final String shortName = "Newt's Horticulture";
	public static final String version = "Beth 1.5";
	
	//Now some universal single-instance miscellaneous shtuff.
	@Instance(modid)
	private static modnh instance;
	@SidedProxy(clientSide="horticulture.NHClientProxy",serverSide="horticulture.NHCommonProxy")
	public static NHCommonProxy proxy;
	public static final Configuration config = new Configuration(new File("config/UniversalElectricity/NewtsHorticulture.cfg"));
	public static final NHCraftingHandler craftingHandler = new NHCraftingHandler();
	
	//Items and Blocks. NH uses 0x4d00-e00 for items (19712-968) and 0x4c0-500 for blocks (1216-80)
	public static final Item itemChisel = new ItemChisel(getItemID("itemChisel",0x4d00));
	public static final Item itemFruit = new ItemFruit(getItemID("itemFruit",0x4d01));
	public static final Item itemExplodingLemon = new ItemExplodingLemon(getItemID("itemExplodingLemon",0x4d02));
	public static final Item itemFlour = new ItemNH(getItemID("itemFlour",0x4d03),"Flour").setIconCoord(2,0);
	public static final Item itemTractor = new ItemTractor(getItemID("itemTractor",0x4d04));
	public static final BlockMillstones blockMillstones = new BlockMillstones(getBlockID("blockMillstones",0x4c0));
	public static final BlockFruitLeaves blockFruitLeaves = new BlockFruitLeaves(getBlockID("blockFruitLeaves",0x4c1));
	public static final BlockFruitLeaves blockFruitLeaves2 = new BlockFruitLeaves(getBlockID("blockFruitLeaves2",0x4c2));
	public static final BlockFruitSapling blockFruitSapling = new BlockFruitSapling(getBlockID("blockFruitSapling",0x4c3));
	public static final BlockFruitSapling blockFruitSapling2 = new BlockFruitSapling(getBlockID("blockFruitSapling2",0x4c4));
	public static final BlockFruitLog blockFruitLog = new BlockFruitLog(getBlockID("blockFruitLog",0x4c5));
	public static final BlockFruitHanging blockFruitHanging = new BlockFruitHanging(getBlockID("blockFruitHanging",0x4c6));
	public static final BlockFruitHanging blockFruitHanging2 = new BlockFruitHanging(getBlockID("blockFruitHanging2",0x4c7));
	public static final BlockFruitFlower blockFruitFlower = new BlockFruitFlower(getBlockID("blockFruitFlower",0x4c8));
	//												0		1		2		3		4		5		6			7	 8			9			10		11		12		13		14		15			0		1		2
	public static final String[] treeFruits = {"Apple","Pear","Apricot","Cherry","Peach","Plum","Mulberry","Date","Fig","Pomegranate","Kumquat","Lemon","Lime","Orange","Banana","Coconut","Mango","Olive","Pecan"};
	public static final String[] vineFruits = {"Grape","Honeydew","Vanilla","Cantaloupe"};
	//												0			1			2			3			4			5
	public static final String[] flowerFruits = {"Strawberry","Peanut","Blackberry","Blueberry","Cranberry","Pineapple"};
	public static final Object[] allFruits;
	
	static{
		ArrayList<String> q = new ArrayList<String>();
		Collections.addAll(q, treeFruits);
		Collections.addAll(q, vineFruits);
		Collections.addAll(q, flowerFruits);
		allFruits = q.toArray();
		blockFruitLeaves2.setDisplaced();
		blockFruitSapling2.setDisplaced();
		blockFruitHanging2.setDisplaced();
	}
	
	@Init
	public void load(FMLInitializationEvent event){
		proxy.init();
		GameRegistry.addRecipe(new ItemStack(itemChisel), new Object[]{"!","\"",'!',Item.stick,'"',Item.ingotIron});
		GameRegistry.addShapelessRecipe(new ItemStack(blockMillstones),new Object[]{itemChisel,Block.stoneSingleSlab,Block.stoneSingleSlab});
		GameRegistry.addShapelessRecipe(new ItemStack(itemExplodingLemon), new Object[]{Block.tnt,new ItemStack(itemFruit,1,11)});
		GameRegistry.addShapelessRecipe(new ItemStack(Block.planks,2),new Object[]{blockFruitLog});
		GameRegistry.registerCraftingHandler(craftingHandler);
		GameRegistry.registerBlock(blockMillstones);
		GameRegistry.registerBlock(blockFruitLeaves, ItemBlockFruitLeaves.class);
		GameRegistry.registerBlock(blockFruitLeaves2, ItemBlockFruitLeaves.class);
		GameRegistry.registerBlock(blockFruitSapling, ItemBlockFruitSapling.class);
		GameRegistry.registerBlock(blockFruitSapling2, ItemBlockFruitSapling.class);
		GameRegistry.registerBlock(blockFruitLog);
		GameRegistry.registerWorldGenerator(new GeneratorFruitTrees());
		LanguageRegistry.addName(itemExplodingLemon, "Lemon");
		LanguageRegistry.addName(blockMillstones, "Millstones");
		EntityRegistry.registerGlobalEntityID(EntityTractor.class, "Tractor", 77);
		NetworkRegistry.instance().registerGuiHandler(this, this.proxy);
		StringTranslate.getInstance().getLanguageList().put("la_RM", "Latina");
		for(int i=0;i<16;++i){
			LanguageRegistry.addName(new ItemStack(blockFruitLeaves,1,i), treeFruits[i]+" Tree Branch");
			LanguageRegistry.addName(new ItemStack(blockFruitSapling,1,i), treeFruits[i]+" Tree Sapling");
			//			LanguageRegistry.instance().addNameForObject(objectToName, lang, name)
		}
		for(int i=16;i<treeFruits.length;++i){
			LanguageRegistry.addName(new ItemStack(blockFruitLeaves2,1,i-16), treeFruits[i]+" Tree Branch");
			LanguageRegistry.addName(new ItemStack(blockFruitSapling2,1,i-16), treeFruits[i]+" Tree Sapling");
		}
		for(int i=0;i<allFruits.length;++i){
			LanguageRegistry.addName(new ItemStack(itemFruit,1,i), (String) allFruits[i]);
		}
		System.out.println("Now comes with built-in Latin support!");
		Chunk q = null;
	}
	
	public static modnh getInstance(){
		return instance;
	}
	
	public static int getItemID(String name,int def){
		config.load();
		Property q = config.get(Configuration.CATEGORY_ITEM, name, def-256);
		config.save();
		return q.getInt(def-256);
	}
	
	public static int getBlockID(String name,int def){
		config.load();
		Property q = config.get(Configuration.CATEGORY_BLOCK, name, def);
		return q.getInt(def);
	}
	
	public void takenFromCrafting(EntityPlayer p,ItemStack i,IInventory inv){
	}
}
