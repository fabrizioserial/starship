package DTO;

import controller.BulletController;
import lombok.Builder;
import lombok.Data;
import strategy.BulletElement;
import strategy.ElectricShotStrategy;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class BulletControllerDTO implements Serializable {
    List<BulletDTO> bullets;

    public BulletController toBulletController() {
        BulletController controller = new BulletController();
        List<BulletElement> list = bullets.stream().map(BulletDTO::toBulletElement).collect(Collectors.toList());
        controller.setFireList(list);
        return controller;
    }
}
