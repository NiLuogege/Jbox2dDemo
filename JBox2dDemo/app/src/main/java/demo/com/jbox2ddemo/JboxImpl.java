package demo.com.jbox2ddemo;

import android.util.Log;
import android.view.View;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import java.util.Random;

/**
 * Created by ${LuoChen} on 2017/7/27 16:02.
 * email:luochen0519@foxmail.com
 */

public class JboxImpl {
    private int Vwidth;//整个View的宽
    private int Vheight;//整个View的高

    private float mDensity = 1f;//刚体的密度
    private float mFriction = 0.8f;//摩擦系数

    private float mRatio = 50;//虚拟世界和绘制区域的比例
    private float mRestitution = 0.5f;//弹性系数(能量损失率)
    private World mWorld;

    private Random mRandom = new Random();
    private float dt = 1 / 60f; //迭代频率
    private int velocityInerations = 5;//速率迭代器
    private int positionIterations = 20;//迭代次数

    public JboxImpl(float density) {
      this.mDensity=density;
        Log.e("JboxImpl", "mDensity:" + mDensity);
    }

    public void setSize(int w, int h) {
        this.Vwidth = w;
        this.Vheight = h;
    }

    public void startWorld() {
        if (mWorld != null) {
            mWorld.step(dt, velocityInerations, positionIterations);
        }
    }



    public void createWorld() {
        if (mWorld == null) {
            mWorld = new World(new Vec2(0, 10.0f));
            updateVertiacalBounds();
            updateHorizontalBounds();
        }
    }

    /**
     * 创建小圆球对应的刚体
     *
     * @param view
     */
    public void creatBody(View view) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.setType(BodyType.DYNAMIC);//一会儿换成KINEMATIC看看

        bodyDef.position.set(switchPositionToBody(view.getX() + (view.getWidth() / 2))
                , switchPositionToBody(view.getY() + (view.getHeight() / 2)));//设置小球的初始位置

        Shape shape = null;
        Boolean isCycle = (Boolean) view.getTag(R.id.jbox_cycle_view);
        if (isCycle != null && isCycle) {
            shape = craeteCircleShape(switchPositionToBody(view.getWidth() / 2));
        } else {
            return;
        }


        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.setShape(shape);
        fixtureDef.friction = mFriction;
        fixtureDef.density = mDensity;
        fixtureDef.restitution = mRestitution;

        Body cycleBody = mWorld.createBody(bodyDef);
        cycleBody.createFixture(fixtureDef);
        view.setTag(R.id.jbox_body_view, cycleBody);
        if (BuildConfig.DEBUG) Log.e("添加", "cycleBody:" + cycleBody);

        cycleBody.setLinearVelocity(new Vec2(mRandom.nextFloat(), mRandom.nextFloat()));
    }






//    private CircleShape craeteCircleShape(float radius) {
//        CircleShape cycleBody = new CircleShape();
//        cycleBody.setRadius(switchPositionToBody(radius));
//        return cycleBody;
//    }


    //创建圆形
    private Shape craeteCircleShape(float radius) {
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(radius);
        return circleShape;
    }


    private void updateVertiacalBounds() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;//不能运动的

        PolygonShape polygonShape = new PolygonShape();
        float width = switchPositionToBody(Vwidth);//这个长方形的宽
        float height = switchPositionToBody(mRatio);//这个长方形的 高
        polygonShape.setAsBox(width, height);//确定为矩形

        FixtureDef fixtureDef = new FixtureDef();//配置top边界
        fixtureDef.shape = polygonShape;//设置形状
        fixtureDef.density = mDensity;//设置刚体的密度
        fixtureDef.friction = mFriction;//设置刚体的摩擦系数
        fixtureDef.restitution = mRestitution;//刚体的能量损失率

        bodyDef.position.set(0, -height);//设置top边界的刚体的位置
        Body topBody = mWorld.createBody(bodyDef);//创建上边界刚体
        topBody.createFixture(fixtureDef);//对刚体进行设置

        bodyDef.position.set(0, switchPositionToBody(Vheight) + height);//设置bottom边界的刚体的位置
        Body bottomBody = mWorld.createBody(bodyDef);
        bottomBody.createFixture(fixtureDef);
    }



    private void updateHorizontalBounds() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;//不能运动的

        PolygonShape shape = new PolygonShape();
        float boxWidth = switchPositionToBody(mRatio);
        float boxHeight = switchPositionToBody(Vheight);
        shape.setAsBox(boxWidth, boxHeight);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = mDensity;
        fixtureDef.friction = 0.8f;
        fixtureDef.restitution = 0.5f;

//        bodyDef.position.set(-boxWidth, 0);
//        Body left = mWorld.createBody(bodyDef);
//        left.createFixture(fixtureDef);

        bodyDef.position.set(switchPositionToBody(Vwidth) + boxWidth, 0);
        Body right = mWorld.createBody(bodyDef);
        right.createFixture(fixtureDef);


        float boxWidth_3 = switchPositionToBody(Vwidth);
        float boxHeight_3 = switchPositionToBody(Vheight);
        PolygonShape shape_3=new PolygonShape();
        Vec2[] vec2s=new Vec2[4];
        vec2s[0]=new Vec2(0,0);
        vec2s[1]=new Vec2(boxWidth_3/2,0);
        vec2s[2]=new Vec2(0,boxHeight_3);
        shape_3.set(vec2s,3);

        FixtureDef fixtureDef_3 = new FixtureDef();
        fixtureDef_3.shape = shape_3;
        fixtureDef_3.density = mDensity;
        fixtureDef_3.friction = 0.8f;
        fixtureDef_3.restitution = 0.5f;

        bodyDef.position.set(0,0);
        Body left_3 = mWorld.createBody(bodyDef);
        left_3.createFixture(fixtureDef_3);
    }






    //view坐标映射为物理的坐标
    private float switchPositionToBody(float viewPosition) {
        return viewPosition / mRatio;
    }

    //物理的坐标映射为iew坐标映射
    private float switchPositionToView(float bodyPosition) {
        return bodyPosition * mRatio;
    }


//    public boolean isBodyView(View view) {
//        Body body = (Body) view.getTag(R.id.jbox_body_view);
//        return body != null;
//    }


    public boolean isBodyView(View view) {
        Body body = (Body) view.getTag(R.id.jbox_body_view);
        return body != null;

    }


    /**
     * 进行一轮迭代模拟
     * dt——模拟的频率
     * positionIterations——迭代次数。迭代越大，模拟越精确、同时性能越低
     */




    public float getViewX(View view) {
        Body body = (Body) view.getTag(R.id.jbox_body_view);
        if (body != null) {
            return switchPositionToView(body.getPosition().x ) - (view.getWidth() / 2);
        }
        return 0;
    }




    public float getViewY(View view) {
        Body body = (Body) view.getTag(R.id.jbox_body_view);
        if (body != null) {
            return switchPositionToView(body.getPosition().y ) - (view.getHeight() / 2);
        }
        return 0;
    }



    public float getViewRotaion(View view) {
        Body body = (Body) view.getTag(R.id.jbox_body_view);
        if (body != null) {
            float angle = body.getAngle();
            return  (angle / 3.14f * 180f) % 360;
        }
        return 0;
    }




    public void applyLinearImpulse(float x, float y, View view) {
        Body body = (Body) view.getTag(R.id.jbox_body_view);
        Vec2 impluse = new Vec2(x,y);
        body.applyLinearImpulse(impluse, body.getPosition(), true); //给body做线性运动 true 运动完之后停止
    }

}
