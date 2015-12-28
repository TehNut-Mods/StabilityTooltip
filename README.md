#StabilityTooltip

A short and sweet mod that allows modpack authors to let their users know which mods are stable, and which mods have a good chance of breaking the everything.

## Config

Using a simple JSON config, you too can be informative! At first run, a default config file is generated that only defines "minecraft" as "STABLE", like so:

    {
	    "minecraft": "STABLE"
	}
	
To add on to this:

1. Add a `,` to the end of the line

2. Create a new line with the format of: `"modid": "stability"`

3. `goto 1`

Your file should now look like this:

    {
	    "minecraft": "STABLE",
		"modid": "stability"
	}

Acceptable stability keys are `STABLE`, `SEMISTABLE`, `UNSTABLE`, and `DANGEROUS`.