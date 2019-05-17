package cn.feuler.bs.basic.dataobject;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

/**
 * Create by: Feuler
 * Date: 2019/3/19
 **/

@ToString
@Data
@Table(name = "bptact")
@Entity
public class Bptact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String recNo;

    @Column(nullable = false, length = 18)
    private String tlrNo;

    @Column(nullable = false)
    private String actType;

    @Column(nullable = false, length = 1)
    private String actSts;

    @Column(nullable = false, length = 23)
    private String dateTime;
}
