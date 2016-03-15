---------------------
-Custom Items 1.7.10-
---------------------
This mod was designed for usage in modpacks (with MineTweaker)
to add simple items for recipes using only simple config.

Kinda license
=============
You can do with this anything you want, excluding lying
that it is your own mod. Yes, you can include it into any
modpack without special permissions.
You also can fork it, but better read 'Contributing'.

Creating custom items pack
==========================
1. Create custom-items directory in .minecraft folder
(near mods and config dirs) if there is no one.
2. Create YOURMODPACK.json file and YOURMODPACK directory.
3. Look at the example directory in this repo to understand how to add
items, textures and translation.
4. Run the game, check if items are added. If items are not added
compare your config with example and look for any errors in log
they will contain 'ERROR] [FML/CustomItems]'
5. Now if everything works, you should have YOURMODPACK.zip near
your config. It is creates automatically every time if there is
YOURMODPACK directory.
6. Only YOURMODPACK.json and YOURMODPACK.zip must be included into
your modpack. You MUST NOT include YOURMODPACK directory into modpack.
Same goes to server custom-items directory.
You can include several of such item packs. They will work together.

Useful info
===========
*If there is no "creativeTabIcon" property in json, creative tab will not be created.
*Look at example carefully, there is everything you need.
Also you may read about json format and how it works.
*And most important thing - this mod is useless itself. It can not add recipes and
it can not add item into ore dictionary. Use MineTweaker mod to do this.
Item id will be <CustomItems:YOURMODPACK_itemNameFromJson>

Contributing
============
There is everything I need in this mod now, but you may need more features
like adding blocks or something else. So, I accept any pull requests.
I am not likely to do anything but fixes, so don't wait for me to write this features for you.