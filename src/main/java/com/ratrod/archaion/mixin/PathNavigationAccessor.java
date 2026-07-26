package com.ratrod.archaion.mixin;

import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PathNavigation.class)
public interface PathNavigationAccessor {

    @Accessor("isStuck")
    void setStuck(boolean stuck);

    @Accessor("lastStuckCheck")
    void setLastStuckCheck(int tick);

    @Accessor("lastStuckCheckPos")
    void setLastStuckCheckPos(Vec3 pos);

    @Accessor("tick")
    int getTick();

    @Accessor("timeoutCachedNode")
    void setTimeoutCachedNode(Object node); // Vec3i is internal

    @Accessor("timeoutTimer")
    void setTimeoutTimer(long timer);

    @Accessor("timeoutLimit")
    void setTimeoutLimit(double limit);

    @Invoker("shouldTargetNextNodeInDirection")
    boolean invokeShouldTargetNextNodeInDirection(Vec3 pos);

    @Invoker("timeoutPath")
    void invokeTimeoutPath();
}
