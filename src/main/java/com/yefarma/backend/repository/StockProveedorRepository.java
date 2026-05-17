package com.yefarma.backend.repository;

import com.yefarma.backend.model.StockProveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StockProveedorRepository extends JpaRepository<StockProveedor, Integer> {
    
    @Query("SELECT s FROM StockProveedor s WHERE s.proveedor.idProveedor = :idProveedor")
    List<StockProveedor> listarPorIdProveedor(@Param("idProveedor") Integer idProveedor);
}