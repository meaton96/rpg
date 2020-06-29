package control;

import lombok.Getter;

@Getter
public class Battle {
    
    private final WalkingStage initScene;
    
    // for balancing
    
    private double enemyHealthMulti;
    private double enemyDamageMulti;
    
    public Battle(WalkingStage initScene) {
        System.out.println("Battle started");
        this.initScene = initScene;
        
    }
    

}
