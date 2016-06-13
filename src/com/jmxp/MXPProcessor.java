package com.jmxp;

import java.awt.Color;

public class MXPProcessor 
{
	private ResultHandler results;
	private MXPState state;
	private MXPParser parser;
	private EntityManager entities;
	private ElementManager elements;
	
	public MXPProcessor(String package_name, String version)
	{
		  //create all the objects...
		  results = new ResultHandler();
		  entities = new EntityManager(false);
		  elements = new ElementManager(null, results, entities);
		  state = new MXPState(results, elements, entities, package_name, version);
		  elements.assignMXPState(state);
		  parser = new MXPParser(state, elements, results);		
	}

	public void processText(String text) throws Exception 
	{
		if (text.isEmpty()) return;
		parser.parse(text);
	}

	public void setDefaultText(String font, int size, boolean _bold,
			boolean _italic, boolean _underline, boolean _strikeout, Color fg,
			Color bg) 
	{
		state.setDefaultText (font, size, _bold, _italic, _underline, _strikeout, fg, bg);		
	}

	public void setHeaderParams(int which, String font, int size,
			boolean _bold, boolean _italic, boolean _underline,
			boolean _strikeout, Color fg, Color bg)
	{
		state.setHeaderParams(which, font, size, _bold, _italic, _underline, _strikeout, fg, bg);		
	}

	public void setDefaultGaugeColor(Color color) 
	{
		state.setDefaultGaugeColor(color);		
	}

	public void setNonProportFont(String font) 
	{
		state.setNonProportFont(font);		
	}

	public void setClient(String name, String version) 
	{
		state.setClient(name,version);
	}

	public boolean hasResult()
	{
		return results.haveResults();		
	}

	public MXPResult nextResult() 
	{
		return results.nextResult();
	}
	
	public void switchToOpen ()
	{
	  state.switchToOpen ();
	}

	public void setScreenProps (int sx, int sy, int wx, int wy, int fx, int fy)
	{
	  state.setScreenProps (sx, sy, wx, wy, fx, fy);
	}

	public void supportsLink (boolean supports)
	{
	  state.supportsLink (supports);
	}

	public void supportsGauge (boolean supports)
	{
	  state.supportsGauge (supports);
	}

	public void supportsSound (boolean supports)
	{
	  state.supportsSound (supports);
	}

	public void supportsStatus (boolean supports)
	{
	  state.supportsStatus (supports);
	}

	public void supportsFrame (boolean supports)
	{
	  state.supportsFrame (supports);
	}

	public void supportsImage (boolean supports)
	{
	  state.supportsImage (supports);
	}

	public void supportsRelocate (boolean supports)
	{
	  state.supportsRelocate (supports);
	}
	
}
