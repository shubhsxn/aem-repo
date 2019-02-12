package com.corteva.model.component.test;

import org.junit.Test;

import com.corteva.core.base.tests.BaseAbstractTest;
import com.corteva.model.component.bean.AccordionListItemBean;
import com.corteva.model.component.bean.ArticleBean;
import com.corteva.model.component.bean.BioDetailBean;
import com.corteva.model.component.bean.CountryBean;
import com.corteva.model.component.bean.ExperienceFragmentBean;
import com.corteva.model.component.bean.FormBean;
import com.corteva.model.component.bean.GalleryImageBean;
import com.corteva.model.component.bean.HeroCarousalBean;
import com.corteva.model.component.bean.ImageRenditionBean;
import com.corteva.model.component.bean.LanguageBean;
import com.corteva.model.component.bean.LinkListBean;
import com.corteva.model.component.bean.ProductBean;
import com.corteva.model.component.bean.ProductDetailBean;
import com.corteva.model.component.bean.ProductFilterBean;
import com.corteva.model.component.bean.ProductFilterChildBean;
import com.corteva.model.component.bean.ProductFilterListBean;
import com.corteva.model.component.bean.ProductRegistrationJsonBean;
import com.corteva.model.component.bean.ProductRegistrationJsonFeatures;
import com.corteva.model.component.bean.ProductRegistrationJsonGeometry;
import com.corteva.model.component.bean.ProductRegistrationJsonProp;
import com.corteva.model.component.bean.SitemapBean;
import com.corteva.model.component.bean.SocialLinkBean;
import com.corteva.model.component.bean.TagBean;
import com.corteva.model.component.bean.VideoBean;
import com.corteva.model.component.models.AbstractSlingModel;
import com.corteva.model.component.models.AnchorLinksNavigationModel;
import com.corteva.model.component.models.CardsContainerModel;
import com.corteva.model.component.models.CharacteristicChart;
import com.corteva.model.component.models.FixedGridModel;
import com.corteva.model.component.models.HybridTableModel;
import com.corteva.model.component.models.ImageModel;
import com.corteva.model.component.models.LinkListModel;
import com.corteva.model.component.models.TwoColumnFeatureModel;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;

/**
 * This is a test class for Getter Setter of Models.
 * 
 * @author Sapient
 * 
 */
public class GetterSetterTest extends BaseAbstractTest {

	/** The Constant ACCESSOR_VALIDATOR. */
	private static final com.openpojo.validation.Validator ACCESSOR_VALIDATOR = ValidatorBuilder.create()
			.with(new GetterTester()).with(new SetterTester()).build();

	/**
	 * Validate accessors.
	 *
	 * @param clazz
	 *            the clazz
	 */
	public static void validateAccessors(final Class<?> clazz) {
		ACCESSOR_VALIDATOR.validate(PojoClassFactory.getPojoClass(clazz));
	}

	/**
	 * Test accesors should access proper field.
	 */
	@Test
	public void testAccesors_shouldAccessProperField() {
		validateAccessors(AnchorLinksNavigationModel.class);
		validateAccessors(CardsContainerModel.class);
		validateAccessors(FixedGridModel.class);
		validateAccessors(LinkListModel.class);
		validateAccessors(AbstractSlingModel.class);
		validateAccessors(ExperienceFragmentBean.class);
		validateAccessors(ImageRenditionBean.class);
		validateAccessors(LinkListBean.class);
		validateAccessors(TwoColumnFeatureModel.class);
		validateAccessors(HeroCarousalBean.class);
		validateAccessors(SocialLinkBean.class);
		validateAccessors(AccordionListItemBean.class);
		validateAccessors(ImageModel.class);
		validateAccessors(TagBean.class);
		validateAccessors(BioDetailBean.class);
		validateAccessors(VideoBean.class);		
		validateAccessors(ArticleBean.class);		
		validateAccessors(ProductBean.class);
		validateAccessors(ProductFilterBean.class);
		validateAccessors(ProductFilterChildBean.class);
		validateAccessors(ProductFilterListBean.class);
		validateAccessors(ProductDetailBean.class);
		validateAccessors(CountryBean.class);
		validateAccessors(FormBean.class);
		validateAccessors(LanguageBean.class);
		validateAccessors(GalleryImageBean.class);
		validateAccessors(ProductRegistrationJsonBean.class);
		validateAccessors(ProductRegistrationJsonFeatures.class);
		validateAccessors(ProductRegistrationJsonGeometry.class);
		validateAccessors(ProductRegistrationJsonProp.class);
		validateAccessors(ImageModel.class);
		validateAccessors(CharacteristicChart.class);
		validateAccessors(HybridTableModel.class);
		validateAccessors(SitemapBean.class);
	}
}