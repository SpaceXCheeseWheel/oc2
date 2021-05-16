package li.cil.oc2.common.bus.device.provider.item;

import li.cil.oc2.api.bus.device.ItemDevice;
import li.cil.oc2.api.bus.device.provider.ItemDeviceQuery;
import li.cil.oc2.api.capabilities.TerminalUserProvider;
import li.cil.oc2.common.Config;
import li.cil.oc2.common.bus.device.item.CloudInterfaceCardItemDevice;
import li.cil.oc2.common.bus.device.provider.util.AbstractItemDeviceProvider;
import li.cil.oc2.common.capabilities.Capabilities;
import li.cil.oc2.common.item.Items;
import net.minecraftforge.common.util.LazyOptional;

import java.util.Optional;

public final class CloudInterfaceCardItemDeviceProvider extends AbstractItemDeviceProvider {
    public CloudInterfaceCardItemDeviceProvider() {
        super(Items.CLOUD_INTERFACE_CARD);
    }

    ///////////////////////////////////////////////////////////////////

    @Override
    protected boolean matches(final ItemDeviceQuery query) {
        return super.matches(query) && getTerminalUserProvider(query).isPresent();
    }

    @Override
    protected Optional<ItemDevice> getItemDevice(final ItemDeviceQuery query) {
        return getTerminalUserProvider(query).map(provider ->
                new CloudInterfaceCardItemDevice(query.getItemStack(), provider));
    }

    @Override
    protected int getItemDeviceEnergyConsumption(final ItemDeviceQuery query) {
        return Config.cloudInterfaceCardEnergyPerTick;
    }

    ///////////////////////////////////////////////////////////////////

    private Optional<TerminalUserProvider> getTerminalUserProvider(final ItemDeviceQuery query) {
        if (query.getContainerTileEntity().isPresent()) {
            final LazyOptional<TerminalUserProvider> capability = query.getContainerTileEntity().get()
                    .getCapability(Capabilities.TERMINAL_USER_PROVIDER);
            if (capability.isPresent()) {
                return capability.resolve();
            }
        }

        if (query.getContainerEntity().isPresent()) {
            final LazyOptional<TerminalUserProvider> capability = query.getContainerEntity().get()
                    .getCapability(Capabilities.TERMINAL_USER_PROVIDER);
            if (capability.isPresent()) {
                return capability.resolve();
            }
        }

        return Optional.empty();
    }
}