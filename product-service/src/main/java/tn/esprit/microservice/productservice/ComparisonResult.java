package tn.esprit.microservice.productservice;

import java.util.List;

public class ComparisonResult {
    private Product myProduct;
    private List<ComparisonProduct> externalMatches;

    public ComparisonResult() {}

    public ComparisonResult(Product myProduct, List<ComparisonProduct> externalMatches) {
        this.myProduct = myProduct;
        this.externalMatches = externalMatches;
    }

    public Product getMyProduct() {
        return myProduct;
    }

    public void setMyProduct(Product myProduct) {
        this.myProduct = myProduct;
    }

    public List<ComparisonProduct> getExternalMatches() {
        return externalMatches;
    }

    public void setExternalMatches(List<ComparisonProduct> externalMatches) {
        this.externalMatches = externalMatches;
    }
}

