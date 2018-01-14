package com.craighorwood.ocus.state;
public class ExtraState extends State
{
	protected ExtrasState parent;
	public ExtraState(ExtrasState parent)
	{
		this.parent = parent;
		hideMouse = true;
	}
}