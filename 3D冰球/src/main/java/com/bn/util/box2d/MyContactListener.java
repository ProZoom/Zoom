package com.bn.util.box2d;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

import com.bn.constant.MyHHData;
import com.bn.view.GameView;

public class MyContactListener implements ContactListener{
	GameView gv;
	public MyContactListener(GameView gv)
	{
		this.gv=gv;
	}
	@Override
	public void beginContact(Contact contact){//¿ªÊ¼´¥¿ØÊ±
		Box2DDoAction.doAction(gv,contact.m_fixtureA.getBody(),contact.m_fixtureB.getBody(),gv.boxBody,gv.ball);
		Box2DDoAction.doCrashAction(contact.m_fixtureA.getBody(),contact.m_fixtureB.getBody(), MyHHData.boxList, gv.ball,gv);
	}

	@Override
	public void endContact(Contact contact) {
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		
	}

}
