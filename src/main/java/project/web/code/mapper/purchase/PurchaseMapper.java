package project.web.code.mapper.purchase;

import project.web.code.dto.OrderListDTO;
import project.web.code.dto.PaymentDTO;
import project.web.code.dto.PurchaseDTO;
import project.web.code.dto.PurchaseListDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PurchaseMapper {
    public String selectNum(); //이번엔 mapper를 같은 패키지가 아닌 resource에 만들어 보겠습니다
    // resource에 만들기 위해서는 한가지 설정이 필요합니다.
    public Integer purchaseInsert(PurchaseDTO dto);
    public int purchaseListInsert(PurchaseListDTO dto);
    public PurchaseDTO purchaseSelect(String purchaseNum);
    public int purchaseGoodsCount(String purchaseNum);
    public String firstGoods(String purchaseNum);
   public int paymentInsert(PaymentDTO dto);
    public int purchaseStatusUpdate(@Param("status") String status,
                                    @Param("purchaseNum") String purchaseNum);
    public List<OrderListDTO> orderList(@Param("memberNum")String memberNum,
                                        @Param("purchaseNum")String purchaseNum);
    public int paymentDelete(String purchaseNum);
    public Integer purchaseOk(String purchaseNum);
}